package sbd.telegram;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


@AllArgsConstructor
@NoArgsConstructor
public class Bot extends TelegramLongPollingBot {
    private static final Logger log = Logger.getLogger(Bot.class);
    public static final SendMessage message = new SendMessage();
    String botName;
    String botToken;

    @Override
    public String getBotUsername() {
        return "InsuranceCompanyBot";
    }

    @Override
    public String getBotToken() {
        return "6735510509:AAGEk_vrWYGF-o5FcyAzV3pJIsdLNN5V3mg";
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            log.debug("updateID: " + update.getUpdateId());
            Long chatId = update.getMessage().getChatId();

            message.setChatId(String.valueOf(chatId));
            String inputText = update.getMessage().getText();

            InlineKeyboard inlineKeyboard = new InlineKeyboard();

            if (inputText.equals("/start")) {
                log.debug(inputText);
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
//                else
                log.debug(inputText);
                execute(inlineKeyboard.buttonsForReg(chatId));
            }
//        TODO 1) сделать путь в случае, если пользователь зарегестрирован ||||
//         2) В последсвии будет запрос на бд, чтобы узнать админ это или клиент
//        else

//        TODO основная зона отвецтвенности бота, после ввода укоманды /key
//         будет выведено кнопки с вариантами задач
            if (inputText.equals("/key")) {
                log.debug(inputText);
                execute(inlineKeyboard.buttonsForKey());
            }
            if (inputText.equals("/rickroll")) {
                log.debug(inputText);
                execute(inlineKeyboard.rickRollMeme());
            }
        } else if (update.hasCallbackQuery()) {
            log.debug("callBackQueryCheck:" + update.getCallbackQuery());
            callbackQueryCheck(update);
        }
    }

    @SneakyThrows
    public void callbackQueryCheck(Update update) {
        message.setReplyMarkup(null);
        Client client = new Client();
        String data = update.getCallbackQuery().getData();
//        if (update.hasMessage() && update.getMessage().hasText()) {
        if (data.equals("Регистрация")) {
            log.debug("Регистрация");
//                String inputText = update.getMessage().getText();
//                client.createRegistration(inputText);
            client.createRegistration();
            execute(printText("Для выбора задач нажмите /key"));
//            }
        }
        if (data.equals("/key")) {
            InlineKeyboard inlineKeyboard = new InlineKeyboard();
            execute(inlineKeyboard.buttonsForKey());
            log.debug("Btn1");
        }
        if (data.equals("Btn2")) {
            log.debug("Btn2");
            execute(printText("Btn2"));
        }

        if (data.equals("Btn3")) {
            log.debug("Btn3");
            execute(printText("Btn3"));
        }
        if (data.equals("rickroll"))
            log.debug("rickroll");
    }

    public SendMessage printText(String text) {
        message.setText(text);
        return message;
    }
}

