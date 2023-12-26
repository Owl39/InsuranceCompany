package sbd.telegram.bot;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import sbd.telegram.controllers.User;
import sbd.telegram.database.DataBaseRedis;
import sbd.telegram.database.DataBaseSql;
import sbd.telegram.database.InputControl;
import sbd.telegram.database.InputState;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import static sbd.telegram.controllers.UserState.*;
import static sbd.telegram.controllers.UserState.NONE;

@AllArgsConstructor
public class Bot extends TelegramLongPollingBot {
    public HashMap<Long, User> userSessions = new HashMap<>();
    InlineKeyboard inlineKeyboard = new InlineKeyboard();
    Serializable result;

    public Bot() {
    }

    private String botName = "InsuranceCompanyBot";
    private String botToken = "6735510509:AAGEk_vrWYGF-o5FcyAzV3pJIsdLNN5V3mg";

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            var user = getSession(chatId);
            InputControl inputControl = new InputControl();
            String inputText = update.getMessage().getText();
            doUserAction(inputText, inputControl, user);
        } else if (update.hasCallbackQuery()) {
            callbackQueryCheck(update);
        }
    }

    @SneakyThrows
    public void callbackQueryCheck(Update update) {
        String dataText = update.getCallbackQuery().getData();
        var chatId = update.getCallbackQuery().getFrom().getId();
        var user = getSession(chatId);
        doAllKeysActions(dataText, user);
    }

    public User getSession(Long chatId) {
        if (userSessions.containsKey(chatId))
            return userSessions.get(chatId);
        var user = new User();
        user.setChatId(chatId);
        userSessions.put(chatId, user);
        return user;
    }

    public void doAllKeysActions(String dataText, User user) {
        Long chatId = user.getChatId();
        ButtonsActions buttonsActions = new ButtonsActions();
        if (user.getState() == KEY || user.getState() == NONE || user.getState() == ADMIN)
            buttonsActions.doKeyAction(dataText, chatId, user);
        if (user.getState() == EDIT || user.getState() == POLICY_DELETE || user.getState() == ADMIN)
            buttonsActions.doEditAction(dataText, chatId, user);
        if (user.getState() == POLICY || user.getState() == POLICY_ADD || user.getState() == NONE || user.getState() == ADMIN)
            buttonsActions.doInsuranceAction(user, dataText, chatId);
        if (user.getState() == POLICY_ADD || user.getState() == POLICY || user.getState() == ADMIN)
            buttonsActions.doInsuranceTechAction(dataText, user);
        if (user.getState() == POLICY_DELETE || user.getState() == ADMIN)
            buttonsActions.doDeleteInsurance(dataText, chatId);
        if (user.getState() == ADMIN || user.getState() == NONE)
            buttonsActions.doAdminAction(dataText, user);
    }

    @SneakyThrows
    public void doUserAction(String inputText, InputControl inputControl, User user) {
        Long chatId = user.getChatId();
        switch (inputText) {
            case "/start":
                onStart(user, inputControl);
                break;
            case "/reg":
                onRegistration(user, inputControl);
                break;
            case "/key":
                onKey(user, inputControl);
                break;
            case "/admin":
                DataBaseRedis dataBaseRedis = new DataBaseRedis();
                if (dataBaseRedis.isAdmin(chatId)) {
                    execute(printText(chatId, "Hello Admin \uD83C\uDD94:" + chatId));
                    onAdminKey(user);
                }
                break;
            default:
                onUserInput(inputText, user, inputControl);
                break;
        }
    }

    @SneakyThrows
    public void onUserInput(String inputText, User user, InputControl inputControl) {
        result = inputControl.inputStringParser(inputText);
        Long chatId = user.getChatId();
        if (user.getState() == REGISTRATION || user.getState() == EDIT) {
            if (result instanceof ArrayList) {
                ArrayList<InputState> states = (ArrayList<InputState>) result;
                if (states.contains(InputState.NONE)) {
                    execute(printText(chatId, "Не можна вводити технічні команди, пусту строку, або відхилятися від фоормату вводу!"));
                    invalidInput(chatId, states);
                    user.setState(REGISTRATION);
                } else {
                    if (inputControl.isValidClient(chatId)) {
                        DataBaseSql dataBaseSql = new DataBaseSql();
                        dataBaseSql.deleteClient(chatId);
                    }
                    execute(printText(chatId, "Це ваші дані: " + result));
                    onHasRegData(user);
                }
            }
        }
        if (user.getState() == ADMIN_KEY)
            execute(printText(chatId, "Твоя строка: " + inputText));
        if (user.getState() == NONE)
            onNone(user);
    }

    @SneakyThrows
    public void invalidInput(Long chatId, ArrayList<InputState> states) {
        for (InputState state : states) {
            switch (state) {
                case SECOND:
                    execute(printText(chatId, "Невірна ініціалізація прізвища"));
                    break;
                case FIRST:
                    execute(printText(chatId, "Невірна ініціалізація ім'я"));
                    break;
                case LAST:
                    execute(printText(chatId, "Невірна ініціалізація по-батькові"));
                    break;
                case EMAIL:
                    execute(printText(chatId, "Невірна ініціалізація електронної пошти"));
                    break;
                case PHONE_NUMBER:
                    execute(printText(chatId, "Невірна ініціалізація контактного номеру телефону"));
                    break;
                default:
                    execute(printText(chatId, "Спробуйте ще раз:"));
                    break;
            }
        }
    }


    @SneakyThrows
    private void onNone(User user) {
        Long chatId = user.getChatId();
        execute(printText(chatId, "Використовуйте меню або натисніть /start, щоб розпочати"));
    }

    @SneakyThrows
    public void onStart(User user, InputControl inputControl) {
        user.setState(START);
        Long chatId = user.getChatId();
        if (!inputControl.isValidClient(chatId))
            execute(printText(chatId, ("\uD83D\uDC4B Вас вітає Insurance Company Bot! Ваш персональный \uD83C\uDD94:" + user.getChatId() +
                    ". \n\nСпочатку треба зареєструватися /reg")));
        else
            execute(printText(chatId, "\uD83D\uDC4B З поверненням! Вас все ще вітає Insurance Company Bot! Нагадую Ваш персональний \uD83C\uDD94:" + user.getChatId() +
                    ". \n\nЩоб перейти в особистий кабінет натисніть /key"));
    }

    @SneakyThrows
    public void onRegistration(User user, InputControl inputControl) {
        user.setState(REGISTRATION);
        Long chatId = user.getChatId();
        if (!inputControl.isValidClient(chatId))
            execute(printText(chatId, "Введіть свої персональні дані в форматі: \n\nПрізвище Ім'я По-батькові Mail@mail Контактний номер телефону"));
        else {
            execute(printText(chatId, "Ви вже зареєстровані. \n\nЩоб перейти в особистий кабінет - натисніть /key"));
        }
    }

    @SneakyThrows
    private void onHasRegData(User user) {
        Long chatId = user.getChatId();
        execute(printText(chatId, "Для переходу в особистий кабінет та збереження даних - натисніть /key. \n\nЩоб змінити ващі данні - натисніть /reg"));
    }

    @SneakyThrows
    public void onEdit(User user) {
        user.setState(EDIT);
        Long chatId = user.getChatId();
        execute(printText(chatId, "Введіть свої персональні дані в форматі: \n\nПрізвище Ім'я По-батькові Mail@mail Контактний номер телефону"));
    }

    @SneakyThrows
    public void onKey(User user, InputControl inputControl) {
        user.setState(KEY);
        Long chatId = user.getChatId();
        inputControl.writeClientTable(user.getChatId());

        if (inputControl.isValidClient(chatId)) {
            InlineKeyboard inlineKeyboard = new InlineKeyboard();
            execute(inlineKeyboard.buttonsForKey(chatId));
        } else {
            execute(printText(chatId, "Ваш акаунт не зареєстрований. \n\nЩоб продовжити - натисніть /reg"));
            user.setState(REGISTRATION);
        }
    }

    @SneakyThrows
    public void onStepBack(User user, InputControl inputControl) {
        onKey(user, inputControl);
    }

    @SneakyThrows
    public void onAdminKey(User user) {
        Long chatId = user.getChatId();
        user.setState(ADMIN);
        InlineKeyboard inlineKeyboard = new InlineKeyboard();
        execute(inlineKeyboard.buttonsForAdminKey(chatId));
    }

    public SendMessage printText(Long chatId, String text) {
        if (chatId != null) {
            var message = new SendMessage(chatId.toString(), text);
            message.setText(text);
            return message;
        } else {
            throw new IllegalArgumentException("chatId is null!");
        }
    }
}
