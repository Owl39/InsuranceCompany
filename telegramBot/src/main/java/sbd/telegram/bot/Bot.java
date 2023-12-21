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
        } else if (update.hasCallbackQuery()) {
            callbackQueryCheck(update);
        }
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
    private void doKeyAction(InlineKeyboard inlineKeyboard, String dataText, Long chatId) {
        var user = getSession(chatId);
        switch (dataText) {
            case "Змінити данні акаунту":
//                TODO сделать запрос к бд на замену конкретных данных
                if (user.getState() == KEY)
                    execute(inlineKeyboard.buttonsForEditUser(chatId));
                break;
            case "Переглянути активні страхування":
                if (user.getState() == KEY)
//                    TODO сделать запрос на чтение данных в бд через внешний ключ insuranceId
                    execute(printText("Будет список страховок"));
                else
                    execute(printText("Ваш акаунт не зареэстрований. Щоб продовжити - натисніть /reg"));
                break;
            case "Оформити страхування":
                if (user.getState() == KEY)
                    execute(inlineKeyboard.buttonsForInsurance(chatId));
                else
                    execute(printText("Ваш акаунт не зарестрований. Щоб продовжити - натисніть /reg"));
                break;
            case "!!!!!Видалити акаунт!!!!!":
                if (DataBase.deleteClient(chatId) > 0) {
                    user.setState(REGISTRATION);
                    execute(printText("Акаунт видалено успішно. Щоб ввести повторно дані: /reg"));
                } else
                    execute(printText("Акаунта не існує. Щоб ввести повторно дані: /reg"));
                break;
            default:
                break;
        }
    }

    @SneakyThrows
    private void doInsuranceAction(InlineKeyboard inlineKeyboard, String dataText, Long chatId) {
        switch (dataText) {
            case "Car":
                DataBase.readInsurances(1);

                break;
            case "Medical":
                DataBase.readInsurances(2);
                break;
            case "Life":
                DataBase.readInsurances(3);
                break;
            case "Real estate":
                DataBase.readInsurances(4);
                break;
            case "Business":
                DataBase.readInsurances(5);
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
                    if (client.stringParser(inputText) == null) {
                        execute(printText("Не можна вводити технічні команди, пусту строку, або відхилятися від фоормату вводу! Спробуйте ще раз"));
                        setUserState(user, REGISTRATION);
                    } else {
                        execute(printText("Це ващі дані: " + client.stringParser(inputText)));
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
                execute(printText("Error input"));
                break;
        }
    }

    @SneakyThrows
    private void setUserState(User user, State state) {
        user.setState(state);
        switch (state) {
            case NONE:
                execute(printText("Використовуйте меню або натисніть /start, щоб розпочати"));
                break;
            case START:
                if (!DataBase.findClientId(user.getChatId()))
                    execute(printText(("\uD83D\uDC4B Вас вітає Insurance Company Bot! Ваш персональный \uD83C\uDD94:" + user.getChatId() +
                            ". \n\nСпочатку треба зареєструватися /reg")));
                else
                    execute(printText("\uD83D\uDC4B З поверненням! Вас все ще вітає Insurance Company Bot! Нагадую Ваш персональний \uD83C\uDD94:" + user.getChatId() +
                            ". \n\nЩоб перейти в особистий кабінет натисніть /key"));
                break;
            case REGISTRATION:
                if (!DataBase.findClientId(user.getChatId()))
                    execute(printText("Введіть свої персональні дані в форматі: \n\nпрізвище ім'я по-батькові mail телефон "));
                else {
                    execute(printText("Ви вже зареєстровані. \n\nЩоб перейти в особистий кабінет - натисніть /key"));
                }
                break;
            case KEY:
                Client client = new Client();
                client.clientTable(user.getChatId());
//                TODO после удаления аккаунта через кноаку нужно пересмотреть логику
                if (DataBase.findClientId(user.getChatId())) {
                    InlineKeyboard inlineKeyboard = new InlineKeyboard();
                    execute(inlineKeyboard.buttonsForKey(user.getChatId()));
                } else {
                    execute(printText("Ваш акаунт не зареэстрований. \n\nЩоб продовжити - натисніть /reg"));
                    user.setState(REGISTRATION);
                }
                break;
            case HAS_REG_DATA:
                execute(printText("Для переходу в особистий кабінет та збереження даних - натисніть /key. \n\nЩоб змінити ващі данні - натисніть /reg"));
                break;
        }
    }

    @SneakyThrows
    private void OnStart(User user) {
        setUserState(user, START);
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

