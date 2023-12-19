package sbd.telegram.bot;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import sbd.telegram.controllers.State;
import sbd.telegram.controllers.User;
import sbd.telegram.database.Client;

import java.util.HashMap;

import static sbd.telegram.controllers.State.*;

@AllArgsConstructor
public class Bot extends TelegramLongPollingBot {
    public HashMap<Long, User> sessions = new HashMap<>();

    public Bot() {
    }

    public static final SendMessage message = new SendMessage();
    public static final Logger log = Logger.getLogger(Bot.class);

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
            log.debug("updateID: " + update.getUpdateId());
            Long chatId = update.getMessage().getChatId();
            var user = getSession(chatId);

            message.setChatId(String.valueOf(chatId));
            String inputText = update.getMessage().getText();

            doUserAction(inputText, user.getChatId());
            //Перенес ТУДУ в StateHandler
        } else if (update.hasCallbackQuery())
            callbackQueryCheck(update);
    }

    @SneakyThrows
    public void callbackQueryCheck(Update update) {
        message.setReplyMarkup(null);
        InlineKeyboard inlineKeyboard = new InlineKeyboard();
        String data = update.getCallbackQuery().getData();
//        TODO чтобы сделать кнопочки
        var chatId = update.getCallbackQuery().getFrom().getId();
        if (data.equals("/key")) {
            log.debug("/key");
            execute(printText("123"));
            execute(inlineKeyboard.buttonsForKey(chatId));
        }
        if (data.equals("Оформити страхування")) {
            log.debug("Btn2");
            execute(inlineKeyboard.buttonsForInsurance(chatId));
        }
        if (data.equals("Переглянути список активних страхувань")) {
            log.debug("Btn3");
            execute(printText("Будет список страховок"));
        }
        if (data.equals("car")) {
            //  log.debug("Btn3");
            execute(printText("нажал car"));
        }
    }

    public User getSession(Long chatId) {
        if (sessions.containsKey(chatId))
            return sessions.get(chatId);
        var user = new User();
        user.setChatId(chatId);
        sessions.put(chatId, user);
        return user;
    }

    @SneakyThrows
    public void doUserAction(String inputText, Long chatId) {
        var user = getSession(chatId);
        switch (inputText) {
            case "/start":
                OnStart(user);
                break;
            case "/reg":
                OnRegistration(user);
                break;
            case "/key":
                OnKey(user);
                break;
            default:
                onUserInput(inputText, user, user.getChatId());
                break;
        }
    }

    @SneakyThrows
    private void onUserInput(String inputText, User user, Long currentId) {
        Client client = new Client();
        switch (user.getState()) {
            case REGISTRATION:
                if (currentId.equals(user.getChatId())) {
                    if (client.stringParser(inputText, user.getChatId()) == null) {
                        execute(printText("Вы ввели некоректное значение. Повторите попытку"));
                        setUserState(user, REGISTRATION);
                    } else {
                        execute(printText("Вы ввели: " + client.stringParser(inputText, user.getChatId())));
                        setUserState(user, HAS_REG_DATA);
                    }
                }
                break;
            case HAS_REG_DATA:
            case NONE:
            case START:
            case KEY:
                setUserState(user, user.getState());
                break;
            default:
                execute(printText("Некоректный ввод"));
                break;
        }
    }

    @SneakyThrows
    private void setUserState(User user, State state) {
        user.setState(state);
        switch (state) {
            case NONE:
                execute(printText("Используйте меню:. Для начала нажмите /start"));
                break;
            case START:
                execute(printText(("\uD83D\uDC4B Вас приветствует Insurance Company Bot. Ваш персональный \uD83C\uDD94:" + user.getChatId() + ". Для начала работы введите /reg")));
                break;
            case REGISTRATION:
                execute(printText("Введите ваш текст:"));
                break;
            case KEY:
                InlineKeyboard inlineKeyboard = new InlineKeyboard();
                execute(inlineKeyboard.buttonsForKey(user.getChatId()));
                break;
            case HAS_REG_DATA:
                execute(printText("Для сохранения /key Для изменения /reg"));
                break;

        }
    }

    @SneakyThrows
    private void OnStart(User user) {
        setUserState(user, State.START);
    }

    @SneakyThrows
    private void OnRegistration(User user) {
        setUserState(user, REGISTRATION);
    }

    @SneakyThrows
    private void OnKey(User user) {
        setUserState(user, KEY);
    }

    private SendMessage printText(String text) {
        message.setText(text);
        return message;
    }
}

