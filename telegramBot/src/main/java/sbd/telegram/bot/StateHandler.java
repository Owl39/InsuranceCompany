package sbd.telegram.bot;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import sbd.telegram.bd.Client;

import static sbd.telegram.bot.Bot.log;
import static sbd.telegram.bot.Bot.message;

public class StateHandler {
    private final Bot bot;
    private final InlineKeyboard inlineKeyboard = new InlineKeyboard();
    private State currentState;
    private Long currentChatId;

    //    Котейнер инициализации
    public StateHandler(Bot bot) {
        this.bot = bot;
        this.currentState = State.START; // Начальное состояние - START
    }

    //    Свитч "контейнер", функция проверяет нынешний currentState и заходит в функции
    //    по нему
    @SneakyThrows
    public void handleState(Update update, String inputText, Long chatId) {
        switch (currentState) {
            case START:
                handleStartState(inputText, chatId);
                break;
            case REGISTRATION:
                handleRegistrationState(inputText, chatId);
                break;
            case WAITING_FOR_REG_INPUT:
                handleRegInput(inputText, chatId);
                break;
            case KEY:
                handleKeyState(update, inputText, chatId);
                break;
            default:
                break;
        }
    }

//            if (inputText.equals("/start")) {
//                log.debug(inputText);
//                TODO Сигнатура для Админа
//                if (chatId.toString().equals("451627964")) {
//                    execute(printText("Welcome Admin"));
//                }
//              else
//                TODO Сигнатура для клиента
//                 --------
//                 if() - если клиент зарегестрирован, else - если клиент новый
//                if (chatId.toString().equals("451627964"))
//                    execute(printText("Welcome Client"));
//                else {
//                log.debug(inputText);
//                execute(inlineKeyboard.buttonForStart(chatId));
//                }
//            }
//        TODO 1) сделать путь в случае, если пользователь зарегестрирован ||||
//         2) В последсвии будет запрос на бд, чтобы узнать админ это или клиент
//        else

    //    Функция обработки команды /start
    @SneakyThrows
    private void handleStartState(String inputText, Long chatId) {
        switch (inputText) {
            case "/start":
                bot.execute(printText(("\uD83D\uDC4B Вас приветствует Insurance Company Bot. Ваш персональный \uD83C\uDD94:" + chatId + ". Для начала работы введите /reg")));
                currentState = State.REGISTRATION;
                break;
            case "/key", "/reg":
                break;
            default:
                bot.execute(printText("Иди проспись"));
                break;
        }
    }

    @SneakyThrows
    private void handleRegistrationState(String inputText, Long chatId) {
        switch (inputText) {
            case "/reg":
                bot.execute(printText("Введите ваш текст:"));
                currentState = State.WAITING_FOR_REG_INPUT;
                currentChatId = chatId;
                break;
            case "/key":
                break;
            case "/start":
                bot.execute(printText("Ты не можешь начать сначала. Для замены значений введи /reg"));
                break;
            default:
                bot.execute(printText("Иди проспись"));
                break;
        }
    }

    @SneakyThrows
    private void handleRegInput(String inputText, Long chatId) {
        // Проверяем, что сообщение пришло от того же пользователя, который начал регистрацию
        if (chatId.equals(currentChatId)) {
            Client client = new Client();
            if (client.createRegistration(inputText) == null) {
                bot.execute(printText("Вы ввели некоректное значение"));
                currentState = State.REGISTRATION;
            } else {
                bot.execute(printText("Вы ввели: " + client.createRegistration(inputText) + " +ClientID:" + chatId));
                currentState = State.KEY; // Переключаем обратно в начальное состояние
                bot.execute(printText("Для продолжения /key"));
            }
        }
    }

    @SneakyThrows
    private void handleKeyState(Update update, String inputText, Long chatId) {
        switch (inputText) {
            case "/key":
                bot.execute(inlineKeyboard.buttonsForKey(update));
                currentState = State.KEY;
                break;
            case "/start":
                bot.execute(printText("Ты не можешь начать сначала. Для замены значений введи /reg"));
                break;
            case "/reg":
                bot.execute(printText("Замена значений!"));
                handleRegistrationState(inputText, chatId);
                break;
            default:
                bot.execute(printText("Иди проспись"));
                break;
        }
    }

    @SneakyThrows
    public void callbackQueryCheck(Update update) {
        message.setReplyMarkup(null);
        String data = update.getCallbackQuery().getData();
//        TODO чтобы сделать кнопочки
//        var userId = update.getCallbackQuery().getFrom().getId();
        if (data.equals("/key")) {
            log.debug("/key");
            bot.execute(printText("123"));
        }
        if (data.equals("Оформити страхування")) {
            log.debug("Btn2");
            bot.execute(printText("После ввода тест"));
        }
        if (data.equals("Btn3")) {
            log.debug("Btn3");
            bot.execute(printText("Btn3"));
        }
    }

    public SendMessage printText(String text) {
        message.setText(text);
        return message;
    }
}

