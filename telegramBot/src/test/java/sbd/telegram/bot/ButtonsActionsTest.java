package sbd.telegram.bot;

import lombok.SneakyThrows;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sbd.telegram.controllers.User;
import sbd.telegram.database.DataBaseSql;

public class ButtonsActionsTest {

    private ButtonsActions buttonsActions;
    private Long chatId;
    private User user;
    private Bot bot;

    private InlineKeyboard inlineKeyboard;

    @Before
    public void setUp() {
        bot = new Bot();
        buttonsActions = new ButtonsActions();
        chatId = 562373389L;
        user = new User();
        inlineKeyboard = new InlineKeyboard();
    }

    @SneakyThrows
    @Test
    public void doKeyAction() {
        buttonsActions.doKeyAction("Змінити данні акаунту", chatId, user);
    }

    @Test
    public void showActiveInsurances() {
        buttonsActions.showActiveInsurances(inlineKeyboard, user, chatId);
    }

    @Test
    public void doAdminAction() {
        user.setChatId(562373389L);
        buttonsActions.doAdminAction("Інформація про працівників", user);
        buttonsActions.doAdminAction("Інформація про клієнтів", user);
        buttonsActions.doAdminAction("Прибутковіть страхувань", user);
    }
}