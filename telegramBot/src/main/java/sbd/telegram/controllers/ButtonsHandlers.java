package sbd.telegram.controllers;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.objects.Update;
import sbd.telegram.database.Client;
import sbd.telegram.bot.Bot;
import sbd.telegram.bot.InlineKeyboard;

public class ButtonsHandlers extends StateHandler {
    private final Bot bot;
    public State currentState;
    public Long currentChatId;
    private final InlineKeyboard inlineKeyboard = new InlineKeyboard();

    public ButtonsHandlers(Bot bot) {
        this.bot = bot;
        this.currentState = State.START; // Начальное состояние - START
    }

    //    Функция обработки команды /start
    @SneakyThrows
    public void handleStartState(String inputText, Long chatId) {
        switch (inputText) {
            case "/start":
                bot.execute(printText(("\uD83D\uDC4B Вас приветствует Insurance Company Bot. Ваш персональный \uD83C\uDD94:" + chatId + ". Для начала работы введите /reg")));
                currentState = State.REGISTRATION;
                currentChatId = chatId;
                break;
            case "/key", "/reg":
                break;
            default:
                bot.execute(printText("Иди проспись"));
                break;
        }
    }

    @SneakyThrows
    public void handleRegistrationState(String inputText, Long chatId) {
        if (chatId.equals(currentChatId)) {
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
    }

    @SneakyThrows
    public void handleRegInput(String inputText, Long chatId) {
        // Проверяем, что сообщение пришло от того же пользователя, который начал регистрацию
        if (chatId.equals(currentChatId)) {
            Client client = new Client();
            if (client.stringParser(inputText, chatId) == null) {
                bot.execute(printText("Вы ввели некоректное значение. Повторите попытку"));
                currentState = State.WAITING_FOR_REG_INPUT;
            } else {
                bot.execute(printText("Вы ввели: " + client.stringParser(inputText, chatId)));
                currentState = State.KEY; // Переключаем обратно в начальное состояние
                bot.execute(printText("Для продолжения /key"));
            }
        }
    }

    @SneakyThrows
    public void handleKeyState(String inputText, Long chatId) {
        if (chatId.equals(currentChatId)) {
            switch (inputText) {
                case "/key":
                    bot.execute(inlineKeyboard.buttonsForKey(chatId));
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
    }
}
