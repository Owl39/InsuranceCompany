package sbd.telegram.bot;

import lombok.SneakyThrows;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sbd.telegram.controllers.User;
import sbd.telegram.database.DataBase;

public class ButtonsActionsTest {

    private ButtonsActions buttonsActions;
    private Long chatId;
    private User user;
    private Bot bot;
    private DataBase dataBase;
    private InlineKeyboard inlineKeyboard;

    @Before
    public void setUp() {
        bot = new Bot();
        buttonsActions = new ButtonsActions();
        chatId = 562373389L;
        user = new User();
        dataBase = new DataBase();
        inlineKeyboard = new InlineKeyboard();
    }

    @SneakyThrows
    @Test
    public void doKeyAction() {
        buttonsActions.doKeyAction("Змінити данні акаунту", chatId, user);
    }

    @Test
    public void doDeleteInsurance() {
        String insuranceType = "Car";
        buttonsActions.doDeleteInsurance(insuranceType, chatId);
    }

    @Test
    public void showActiveInsurances() {
        buttonsActions.showActiveInsurances(inlineKeyboard, user, chatId);
    }

    @Test
    public void doInsuranceTechAction() {
        buttonsActions.doInsuranceTechAction("Додати поліс", user);
    }

    @Test
    public void doAdminAction() {
        user.setChatId(562373389L);
        buttonsActions.doAdminAction("Інформація про працівників", user);
        buttonsActions.doAdminAction("Інформація про клієнтів", user);
        buttonsActions.doAdminAction("Прибутковіть страхувань", user);
    }
}