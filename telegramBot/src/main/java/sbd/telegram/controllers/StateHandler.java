package sbd.telegram.controllers;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import sbd.telegram.bot.Bot;

import static sbd.telegram.bot.Bot.log;
import static sbd.telegram.bot.Bot.message;

public class StateHandler {
    private Bot bot;
    private ButtonsHandlers buttonsHandlers;

    //    Котейнер инициализации
    public StateHandler(Bot bot) {
        this.bot = bot;
        buttonsHandlers = new ButtonsHandlers(bot);
    }

    public StateHandler() {
    }

    //    Свитч "контейнер", функция проверяет нынешний currentState и заходит в функции
    //    по нему
    @SneakyThrows
    public void handleState(Update update, String inputText, Long chatId) {

        switch (buttonsHandlers.currentState) {
            case START:
                buttonsHandlers.handleStartState(inputText, chatId);
                break;
            case REGISTRATION:
                buttonsHandlers.handleRegistrationState(inputText, chatId);
                break;
            case WAITING_FOR_REG_INPUT:
                buttonsHandlers.handleRegInput(inputText, chatId);
                break;
            case KEY:
                buttonsHandlers.handleKeyState(update, inputText, chatId);
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

