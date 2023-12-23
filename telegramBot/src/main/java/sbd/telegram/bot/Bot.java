package sbd.telegram.bot;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import sbd.telegram.controllers.User;
import sbd.telegram.database.Client;

import java.util.HashMap;

import static sbd.telegram.controllers.UserState.*;

@AllArgsConstructor
public class Bot extends TelegramLongPollingBot {
    public HashMap<Long, User> userSessions = new HashMap<>();
    InlineKeyboard inlineKeyboard = new InlineKeyboard();

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
            Client client = new Client();
            String inputText = update.getMessage().getText();
            doUserAction(inputText, client, user);
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
        if (user.getState() == POLICY || user.getState() == NONE || user.getState() == ADMIN)
            buttonsActions.doInsuranceAction(user, dataText, chatId);
        if (user.getState() == POLICY_ADD || user.getState() == ADMIN)
            buttonsActions.doInsuranceTechAction(dataText, user);
        if (user.getState() == POLICY_DELETE || user.getState() == ADMIN)
            buttonsActions.doDeleteInsurance(dataText, chatId);
        if (user.getState() == ADMIN || user.getState() == NONE)
            buttonsActions.doAdminAction(dataText, user);
    }

    @SneakyThrows
    public void doUserAction(String inputText, Client client, User user) {
        Long chatId = user.getChatId();
        switch (inputText) {
            case "/start":
                OnStart(user, client);
                break;
            case "/reg":
                OnRegistration(user, client);
                break;
            case "/key":
                OnKey(user, client);
                break;
            case "/admin":
                DataBase dataBase = new DataBase();
                if (dataBase.isAdmin(chatId))
                    OnAdminKey(user);
                break;
            default:
                onUserInput(inputText, user, client);
                break;
        }
    }

    @SneakyThrows
    private void onUserInput(String inputText, User user, Client client) {
        Long chatId = user.getChatId();
        if (user.getState() == REGISTRATION) {
            if (client.stringParser(inputText) == null) {
                execute(printText(chatId, "Не можна вводити технічні команди, пусту строку, або відхилятися від фоормату вводу! Спробуйте ще раз"));
                user.setState(REGISTRATION);
            } else {
                execute(printText(currentId, "Це ващі дані: " + client.stringParser(inputText)));
                OnHasRegData(user);
            }
        }
        if (user.getState() == ADMIN_KEY)
            execute(printText(chatId, "Твоя строка: " + inputText));
        if (user.getState() == NONE)
            OnNone(user);
    }

    @SneakyThrows
    private void OnNone(User user) {
        Long chatId = user.getChatId();
        execute(printText(chatId, "Використовуйте меню або натисніть /start, щоб розпочати"));
    }

    @SneakyThrows
    private void OnHasRegData(User user) {
        Long chatId = user.getChatId();
        execute(printText(chatId, "Для переходу в особистий кабінет та збереження даних - натисніть /key. \n\nЩоб змінити ващі данні - натисніть /reg"));
    }

    @SneakyThrows
    private void OnStart(User user, Client client) {
        user.setState(START);
        Long chatId = user.getChatId();

        if (!client.isValidClient(chatId))
            execute(printText(chatId, ("\uD83D\uDC4B Вас вітає Insurance Company Bot! Ваш персональный \uD83C\uDD94:" + user.getChatId() +
                    ". \n\nСпочатку треба зареєструватися /reg")));
        else
            execute(printText(chatId, "\uD83D\uDC4B З поверненням! Вас все ще вітає Insurance Company Bot! Нагадую Ваш персональний \uD83C\uDD94:" + user.getChatId() +
                    ". \n\nЩоб перейти в особистий кабінет натисніть /key"));
    }

    @SneakyThrows
    private void OnRegistration(User user, Client client) {
        user.setState(REGISTRATION);
        Long chatId = user.getChatId();
        if (!client.isValidClient(chatId))
            execute(printText(chatId, "Введіть свої персональні дані в форматі: \n\nпрізвище ім'я по-батькові mail телефон "));
        else {
            execute(printText(chatId, "Ви вже зареєстровані. \n\nЩоб перейти в особистий кабінет - натисніть /key"));
        }
    }

    @SneakyThrows
    private void OnKey(User user, Client client) {
        user.setState(KEY);
        Long chatId = user.getChatId();
        client.writeClientTable(user.getChatId());

        if (client.isValidClient(chatId)) {
            InlineKeyboard inlineKeyboard = new InlineKeyboard();
            execute(inlineKeyboard.buttonsForKey(chatId));
        } else {
            execute(printText(chatId, "Ваш акаунт не зареєстрований. \n\nЩоб продовжити - натисніть /reg"));
            user.setState(REGISTRATION);
        }
    }

    @SneakyThrows
    private void OnAdminKey(User user) {
        Long chatId = user.getChatId();
        execute(printText(chatId, "Hello Admin \uD83C\uDD94:" + user.getChatId()));
        user.setState(ADMIN);
        InlineKeyboard inlineKeyboard = new InlineKeyboard();
        execute(inlineKeyboard.buttonsForAdminKey(chatId));
    }

    @SneakyThrows
    public void onStepBack(User user, Client client) {
        OnKey(user, client);
    }

    public SendMessage printText(Long chatId, String text) {
        var message = new SendMessage(chatId.toString(), text);
        message.setText(text);
        return message;
    }
}

