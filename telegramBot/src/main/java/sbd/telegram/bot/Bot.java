package sbd.telegram.bot;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import sbd.telegram.controllers.State;
import sbd.telegram.controllers.User;
import sbd.telegram.database.Client;
import sbd.telegram.database.DataBase;

import java.util.HashMap;

import static sbd.telegram.controllers.State.*;

@AllArgsConstructor
public class Bot extends TelegramLongPollingBot {
    public HashMap<Long, User> sessions = new HashMap<>();

    public Bot() {
    }

    public static final SendMessage message = new SendMessage();

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
            message.setChatId(String.valueOf(chatId));

            String inputText = update.getMessage().getText();
            doUserAction(inputText, user.getChatId());
        } else if (update.hasCallbackQuery())
            callbackQueryCheck(update);
    }

    @SneakyThrows
    public void callbackQueryCheck(Update update) {
        message.setReplyMarkup(null);
        String dataText = update.getCallbackQuery().getData();
        var chatId = update.getCallbackQuery().getFrom().getId();
        InlineKeyboard inlineKeyboard = new InlineKeyboard();
        doKeyAction(inlineKeyboard, dataText, chatId);
        doInsuranceAction(inlineKeyboard, dataText, chatId);
        doForEditUserAction(inlineKeyboard, dataText, chatId);
    }

    @SneakyThrows
    private void doKeyAction(InlineKeyboard inlineKeyboard, String dataText, Long chatID) {
        switch (dataText) {
            case "Змінити данні акаунту":
                execute(inlineKeyboard.buttonsForEditUser(chatID));
                break;
            case "Переглянути активні страхування":
                execute(printText("Будет список страховок"));
                break;
            case "Оформити страхування":
                execute(inlineKeyboard.buttonsForInsurance(chatID));
                break;
            case "!!!!!Видалити акаунт!!!!!":
                DataBase.deleteClient(chatID);
                //                TODO заменить стейт на REG
                execute(printText("Запись успешно удалена из таблицы client."));
//                TODO ДОБАВИТЬ ВЫВОД ПРО отцуцтвие записей
                break;
            default:
                break;
        }
    }

    @SneakyThrows
    private void doInsuranceAction(InlineKeyboard inlineKeyboard, String dataText, Long chatId) {
        switch (dataText) {
            case "Car":
                break;
            case "Medical":
                break;
            case "Life":
                break;
            case "Real estate":
                break;
            case "Business":
                break;
            default:
                break;
        }
    }

    @SneakyThrows
    private void doForEditUserAction(InlineKeyboard inlineKeyboard, String dataText, Long chatId) {
        switch (dataText) {
            case "Ім'я":
                break;
            case "Mail":
                break;
            case "Номер телефону":
                break;
            default:
                break;
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
                execute(printText("Используйте меню. Для начала нажмите /start"));
                break;
            case START:
                execute(printText(("\uD83D\uDC4B Вас приветствует Insurance Company Bot. Ваш персональный \uD83C\uDD94:" + user.getChatId() + ". Для начала работы введите /reg")));
                break;
            case REGISTRATION:
                if (!DataBase.findeClientId(user.getChatId()))
                    execute(printText("Введите ваш текст:"));
                else {
                    execute(printText("Зареган /key"));
                    OnKey(user);
                }
                break;
            case KEY:
                Client client = new Client();
                client.clientTable(user.getChatId());
//                TODO после удаления аккаунта через кноаку нужно пересмотреть логику
                if (DataBase.findeClientId(user.getChatId())) {
                    InlineKeyboard inlineKeyboard = new InlineKeyboard();
                    execute(inlineKeyboard.buttonsForKey(user.getChatId()));
                }
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

    public SendMessage printText(String text) {
        message.setText(text);
        return message;
    }
}

