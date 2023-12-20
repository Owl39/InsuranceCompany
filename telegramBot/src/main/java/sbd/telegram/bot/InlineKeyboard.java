package sbd.telegram.bot;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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

        List<InlineKeyboardButton> row0 = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    row0.add(createButton("Змінити данні акаунту"));
                    break;
                case 1:
                    row2.add(createButton("Переглянути активні страхування"));
                    break;
                case 2:
                    row1.add(createButton("Оформити страхування"));
                    break;
                case 3:
                    row3.add(createButton("!!!!!Видалити акаунт!!!!!"));
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
    public SendMessage buttonsForInsurance(Long chatId) {
        message.setText("Оберіть тип страхування:");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> row0 = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            switch (i) {
                case 0:
                    row0.add(createButton("Car"));
                    break;
                case 1:
                    row0.add(createButton("Medical"));
                    break;
                case 2:
                    row1.add(createButton("Life"));
                    break;
                case 3:
                    row1.add(createButton("Real estate"));
                    break;
                case 4:
                    row2.add(createButton("Business"));
                    break;
                default:
                    break;
            }
        }
        rowsInline.add(row0);
        rowsInline.add(row1);
        rowsInline.add(row2);

        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        message.setChatId(chatId.toString());
        return message;
    }

    public SendMessage buttonsForEditUser(Long chatId) {
        message.setText("Як даних:");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            switch (i) {
                case 0:
                    row.add(createButton("Ім'я"));
                    break;
                case 1:
                    row.add(createButton("Mail"));
                    break;
                case 2:
                    row.add(createButton("Номер телефону"));
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
