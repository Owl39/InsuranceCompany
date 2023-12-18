package sbd.telegram.bot;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class InlineKeyboard {
    public static final SendMessage message = new SendMessage();
    @SneakyThrows
    public SendMessage buttonsForKey(Update update) {
        Long chatId = update.getMessage().getChatId();
        message.setText("Выберать вариант работы:");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            switch (i) {
                case 0:
                    row.add(createButton("/key"));
                    break;
                case 1:
                    row.add(createButton("Оформити страхування"));
                    break;
                case 2:
                    row.add(createButton("Btn3"));
                    break;
                default:
                    break;
            }
        }
        rowsInline.add(row);

        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        message.setChatId(chatId.toString());
        return message;
    }

    public InlineKeyboardButton createButton(String text) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(text);
        return button;
    }
}
