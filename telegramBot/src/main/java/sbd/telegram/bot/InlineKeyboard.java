package sbd.telegram.bot;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import sbd.telegram.controllers.UserState;

import java.util.ArrayList;
import java.util.List;

public class InlineKeyboard {
    public static final SendMessage message = new SendMessage();

    @SneakyThrows
    public SendMessage buttonsForKey(Long chatId) {
        message.setText("Обрати варіант роботи:");

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
        rowsInline.add(row0);
        rowsInline.add(row1);
        rowsInline.add(row2);
        rowsInline.add(row3);

        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        message.setChatId(chatId.toString());
        return message;
    }

    public SendMessage buttonsForEditUser(Long chatId) {
        message.setText("Змінити:");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> row0 = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            switch (i) {
                case 0:
                    row0.add(createButton("Ім'я"));
                    break;
                case 1:
                    row0.add(createButton("Mail"));
                    break;
                case 2:
                    row0.add(createButton("Номер телефону"));
                    break;
                case 3:
                    row1.add(createButton("Видалити поліс"));
                    break;
                case 4:
                    row2.add(createButton("Повернутися в головне меню"));
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

    @SneakyThrows
    public SendMessage buttonsForInsurance(Long chatId, ArrayList<String> arrayOfTypes) {
        message.setText("Переглянути інформацію про страхування:");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> row0 = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        for (int i = 0; i < arrayOfTypes.size()+1; i++) {
            switch (i) {
                case 0:
                    row0.add(createButton(arrayOfTypes.get(i)));
                    break;
                case 1:
                    row0.add(createButton(arrayOfTypes.get(i)));
                    break;
                case 2:
                    row1.add(createButton(arrayOfTypes.get(i)));
                    break;
                case 3:
                    row1.add(createButton(arrayOfTypes.get(i)));
                    break;
                case 4:
                    row2.add(createButton(arrayOfTypes.get(i)));
                    break;
                case 5:
                    row3.add(createButton("Повернутися в головне меню"));
                default:
                    break;
            }
        }
        rowsInline.add(row0);
        rowsInline.add(row1);
        rowsInline.add(row2);
        rowsInline.add(row3);

        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        message.setChatId(chatId.toString());
        return message;
    }

    @SneakyThrows
    public SendMessage insurancesIsRelevant(Long chatId, UserState state) {
        if (UserState.POLICY_CHECK == state)
            message.setText("Переглянути ще варіанти полісів?");
        else
            message.setText("Дана страховка вже існує в списку активних");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            switch (i) {
                case 0:
                    row.add(createButton("Переглянути ще поліси"));
                    break;
                case 1:
                    row.add(createButton("Повернутися в головне меню"));
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
    public SendMessage buttonsForAddingInsurance(Long chatId) {
        message.setText("Оберіть функцію:");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            switch (i) {
                case 0:
                    row.add(createButton("Додати поліс"));
                    break;
                case 1:
                    row.add(createButton("Повернутися в головне меню"));
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
    public SendMessage buttonsForActiveInsurance(Long chatId, ArrayList<String> arrayOfTypes) {
        message.setText("Оберіть страховку, яку видалити:");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> row0 = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        for (String type : arrayOfTypes)
            row0.add(createButton(type));
        row1.add(createButton("Повернутися в головне меню"));
        rowsInline.add(row0);
        rowsInline.add(row1);

        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        message.setChatId(chatId.toString());
        return message;
    }

    @SneakyThrows
    public SendMessage buttonsForAdminKey(Long chatId) {
        message.setText("Оберіть функцію:");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> row0 = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            switch (i) {
                case 0:
                    row0.add(createButton("Інформація про працівників"));
                    break;
                case 1:
                    row1.add(createButton("Інформація про клієнтів"));
                    break;
                case 2:
                    row2.add(createButton("Прибутковіть страхувань"));
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

    public InlineKeyboardButton createButton(String text) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(text);
        return button;
    }
}
