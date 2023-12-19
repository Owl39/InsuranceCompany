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
    public SendMessage buttonsForKey(Long chatId) {
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

    @SneakyThrows
    public SendMessage buttonsForInsurance(Long userId) {

        message.setText("Оберіть тип страхування:");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        for (int i = 0; i < 6; i++)
        {
            switch (i)
            {
                case 1:
                    row.add(createButton("car"));
                    break;
                case 2:
                    row.add(createButton("medical"));
                    break;
                case 3:
                    row.add(createButton("life"));
                    break;
                case 4:
                    row.add(createButton("real estate"));
                    break;
                case 5:
                    row.add(createButton("business"));
                    break;
                default:
                    break;
            }
        }
        rowsInline.add(row);

        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        message.setChatId(userId.toString());
        return message;
    }

    public InlineKeyboardButton createButton(String text) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(text);
        return button;
    }
}
