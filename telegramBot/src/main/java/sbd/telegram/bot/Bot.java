package sbd.telegram.bot;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import sbd.telegram.controllers.User;
import sbd.telegram.database.Client;

import java.util.HashMap;

import static sbd.telegram.controllers.State.*;

@AllArgsConstructor
public class Bot extends TelegramLongPollingBot {
    public HashMap<Long, User> sessions = new HashMap<>();

    private ButtonsActions buttonsActions;

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
            doUserAction(inputText, user.getChatId(), client, user);
        } else if (update.hasCallbackQuery()) {
            callbackQueryCheck(update);
        }
    }

    @SneakyThrows
    public void callbackQueryCheck(Update update) {
        String dataText = update.getCallbackQuery().getData();
        var chatId = update.getCallbackQuery().getFrom().getId();
        var user = getSession(chatId);
        InlineKeyboard inlineKeyboard = new InlineKeyboard();
        buttonsActions = new ButtonsActions();

        doAllKeysActions(inlineKeyboard, dataText, chatId, user);
    }

    public User getSession(Long chatId) {
        if (sessions.containsKey(chatId))
            return sessions.get(chatId);
        var user = new User();
        user.setChatId(chatId);
        sessions.put(chatId, user);
        return user;
    }

    public void doAllKeysActions(InlineKeyboard inlineKeyboard, String dataText, Long chatId, User user) {
        if (user.getState() == KEY || user.getState() == NONE) {
            buttonsActions.doKeyAction(inlineKeyboard, dataText, chatId, user);
            buttonsActions.doForEditUserAction(inlineKeyboard, dataText, chatId, user);
        }
        if (user.getState() == KEY || user.getState() == NONE || user.getState() == POLICY)
            buttonsActions.doInsuranceAction(inlineKeyboard, user, dataText, chatId);
        if (user.getState() == POLICY){
            buttonsActions.doInsuranceTechAction(dataText, user);
        }
    }

    @SneakyThrows
    public void doUserAction(String inputText, Long chatId, Client client, User user) {
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
            default:
                onUserInput(inputText, user, chatId, client);
                break;
        }
    }

    @SneakyThrows
    private void onUserInput(String inputText, User user, Long currentId, Client client) {
        if (user.getState() == REGISTRATION) {
            if (client.stringParser(inputText) == null) {
                execute(printText(currentId, "Не можна вводити технічні команди, пусту строку, або відхилятися від фоормату вводу! Спробуйте ще раз"));
                user.setState(REGISTRATION);
            } else {
                execute(printText(currentId, "Це ващі дані: " + client.stringParser(inputText)));
                OnHasRegData(user);
            }
        }
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
            execute(inlineKeyboard.buttonsForKey(user.getChatId()));
        } else {
            execute(printText(chatId, "Ваш акаунт не зареєстрований. \n\nЩоб продовжити - натисніть /reg"));
            user.setState(REGISTRATION);
        }
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

