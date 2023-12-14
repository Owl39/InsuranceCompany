package sbd.telegram;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@AllArgsConstructor
@NoArgsConstructor
public class Bot extends TelegramLongPollingBot {
    private static final Logger log = Logger.getLogger(Bot.class);
    private static final SendMessage message = new SendMessage();
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

    @Override
    public void onUpdateReceived(Update update) {
//        var message = update.getMessage();
//        System.out.println(message.getText());

        log.debug("Receive new Update. updateID: " + update.getUpdateId());
        Long chatId = update.getMessage().getChatId();


        message.setChatId(String.valueOf(chatId));
        String inputText = update.getMessage().getText();
        if (inputText.startsWith("/start")) {
            log.debug(inputText);
            createClient(chatId);
        }
//        TODO 1) сделать путь в случае, если пользователь зарегестрирован ||||
//         2) В последсвии будет запрос на бд, чтобы узнать админ это или клиент
//        else

//        TODO основная зона отвецтвенности бота, после ввода укоманды /key
//         будет выведено кнопки с вариантами задач
//         https://habr.com/ru/articles/746370/
        if (inputText.equals("/key")){
            log.debug(inputText);
            textException("Дальше кнопочки сделать");
        }
    }

    public void textException(String text){
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void createClient(Long chatId) {
        textException ("Вас приведствует Insurance Company Bot. Ваш персональный id:" + chatId+ "\nДля начала работы введите /key");
//        TODO сделать запрос в бд, чтобы внести всю информацию сразу
    }
}
