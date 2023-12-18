package sbd.telegram.bot;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@AllArgsConstructor
public class Bot extends TelegramLongPollingBot {
    private StateHandler stateHandler;

    public Bot() {
        this.stateHandler = new StateHandler(this); // Передача самого себя в StateHandler
    }

    public static final SendMessage message = new SendMessage();
    static final Logger log = Logger.getLogger(Bot.class);

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

            message.setChatId(String.valueOf(chatId));
            String inputText = update.getMessage().getText();

            stateHandler.handleState(update, inputText, chatId);
            //Перенес ТУДУ в StateHandler
        } else if (update.hasCallbackQuery()) {
            stateHandler.callbackQueryCheck(update);
        }
    }
}

