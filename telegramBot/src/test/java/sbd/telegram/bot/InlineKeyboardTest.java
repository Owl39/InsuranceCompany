package sbd.telegram.bot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InlineKeyboardTest {
    private InlineKeyboard inlineKeyboard;

    @BeforeEach
    public void setUp() {
        inlineKeyboard = new InlineKeyboard();
    }

    @Test
    public void buttonsForKeyTest() {
        SendMessage message = inlineKeyboard.buttonsForKey(123456L);
        InlineKeyboardMarkup markup = (InlineKeyboardMarkup) message.getReplyMarkup();
        List<List<InlineKeyboardButton>> rows = markup.getKeyboard();
        assertEquals("Змінити данні акаунту", rows.get(0).get(0).getText());
        assertEquals("Оформити страхування", rows.get(1).get(0).getText());
    }

    @Test
    public void createButtonTest() {
        InlineKeyboard inlineKeyboard = new InlineKeyboard();

        InlineKeyboardButton button1 = inlineKeyboard.createButton("Test Button 1");
        InlineKeyboardButton button2 = inlineKeyboard.createButton("Test Button 2");

        assertEquals("Test Button 1", button1.getText());
        assertEquals("Test Button 1", button1.getCallbackData());
        assertEquals("Test Button 2", button2.getText());
        assertEquals("Test Button 2", button2.getCallbackData());
    }
}
