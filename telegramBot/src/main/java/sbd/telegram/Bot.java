package sbd.telegram;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;


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

    public SendMessage printText(String text) {
        message.setText(text);
        return message;
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.debug("Receive new Update. updateID: " + update.getUpdateId());
        Long chatId = update.getMessage().getChatId();

        message.setChatId(String.valueOf(chatId));
        String inputText = update.getMessage().getText();
        if (inputText.equals("/start")) {
            log.debug(inputText);
            createClient(chatId);
        }
//        TODO 1) сделать путь в случае, если пользователь зарегестрирован ||||
//         2) В последсвии будет запрос на бд, чтобы узнать админ это или клиент
//        else

//        TODO основная зона отвецтвенности бота, после ввода укоманды /key
//         будет выведено кнопки с вариантами задач
//         https://habr.com/ru/articles/746370/
//        String data = update.getCallbackQuery().getData();
        if (inputText.equals("/key")) {
            log.debug(inputText);
            try {
                execute(buttonsPopUp());
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
//            if (data.equals("Кнопка1")) {
//                try {
//                    execute(printText("111"));
//                } catch (TelegramApiException e) {
//                    throw new RuntimeException(e);
//                }
//            }
        }
    }

    //    TODO Вынести в отдельный класс наверное
    @SneakyThrows
    public void createClient(Long chatId) {
        execute(printText("Вас приведствует Insurance Company Bot. Ваш персональный id:" + chatId + "\nДля начала работы введите /key"));
        //        TODO сделать запрос в бд, чтобы внести всю информацию сразу
    }

    @SneakyThrows
    public SendMessage buttonsPopUp() {
        printText("Выберете варианты работы");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Кнопка1");
//        button1.setUrl("https://www.youtube.com/watch?v=dQw4w9WgXcQ&t=0s");
        button1.setCallbackData("Кнопка1");
        rowInline1.add(button1);

        rowsInline.add(rowInline1);

        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        return message;
    }
}
