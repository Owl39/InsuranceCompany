package sbd.telegram;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
public class InlineKeyboard extends Bot{
    @SneakyThrows
    public SendMessage buttonsForKey() {
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
        return message;
    }

    @SneakyThrows
    public SendMessage buttonsForInsurance() {
        message.setText("Введіть необхідну інформацію:");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            switch (i) {
                case 0:
                    row.add(createButton("ПІБ"));
                    break;
                default:
                    break;
            }
        }
        rowsInline.add(row);

        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        return message;
    }



    public SendMessage buttonForStart(Long chatId) {
        message.setText("\uD83D\uDC4B Вас приветствует Insurance Company Bot. Ваш персональный \uD83C\uDD94:" + chatId);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();

        row.add(createButton("Регистрация"));
        rowsInline.add(row);
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        return message;
    }

    public InlineKeyboardButton createButton(String text) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(text);
        return button;
    }

    @SneakyThrows
    public SendMessage rickRollMeme() {
        message.setText("Наслаждайся");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Начать кайфовать");
        button.setUrl("https://www.youtube.com/watch?v=dQw4w9WgXcQ&t=0s");
        button.setCallbackData("rickroll");

        row.add(button);
        rowsInline.add(row);
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        return message;
    }
}
