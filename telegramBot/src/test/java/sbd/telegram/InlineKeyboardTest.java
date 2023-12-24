package sbd.telegram;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import sbd.telegram.bot.InlineKeyboard;

import java.util.List;

public class InlineKeyboardTest {
    private InlineKeyboard inlineKeyboard;

    @BeforeEach
    public void setUp() {
        inlineKeyboard = new InlineKeyboard();
    }

    @Test
    public void testButtonsForKey() {
        SendMessage message = inlineKeyboard.buttonsForKey(123456L);
        InlineKeyboardMarkup markup = (InlineKeyboardMarkup) message.getReplyMarkup();
        List<List<InlineKeyboardButton>> rows = markup.getKeyboard();
        Assertions.assertEquals("Змінити данні акаунту", rows.get(0).get(0).getText());
        Assertions.assertEquals("Оформити страхування", rows.get(1).get(0).getText());
    }

    @Test
    public void testButtonsForEditUser() {
    }
}
