package sbd.telegram.bot;

import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Nested;
import sbd.telegram.controllers.User;
import sbd.telegram.controllers.UserState;
import sbd.telegram.database.DataBaseSql;
import sbd.telegram.database.InputControl;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static sbd.telegram.controllers.UserState.KEY;

@Nested
public class BotTest {
    private Bot bot;
    private User user;

    @Before
    public void setUp(){
        this.bot = new Bot();
        this.user = new User();
    }

    @Test
    public void getSessionValid() {
        Long chatId = 12345L;
        user.setChatId(chatId);
        HashMap<Long, User> userSessions = new HashMap<>();
        userSessions.put(chatId, user);
        bot.userSessions = userSessions;
        user = bot.getSession(chatId);
        assertNotNull(user);
        assertEquals(chatId, user.getChatId());
    }

    @Test
    public void getSessionNotValid() {
        Long chatId = 12345L;;
        user.setChatId(chatId);
        HashMap<Long, User> userSessions = new HashMap<>();
        userSessions.put(chatId, user);
        bot.userSessions = userSessions;
        user = bot.getSession(chatId);
        assertNotNull(user);
        assertEquals(chatId, user.getChatId());
    }

    @Test
    public void doAllKeysActions() {
        user.setState(KEY);
        String dataText = "someData";
        bot.doAllKeysActions(dataText, user);
        assertEquals(KEY, user.getState());
    }

    @Test
    public void doUserAction() {
        DataBaseSql.connectSQLite();
        user.setChatId(562373389L);
        InputControl inputControl = new InputControl();
        String inputText = "/start";
        bot.doUserAction(inputText, inputControl, user);
        assertEquals(UserState.START, user.getState());
    }

    @Test
    public void onStartValid() {
        user.setChatId(562373389L);
        user.setState(UserState.START);
        InputControl inputControl = mock(InputControl.class);
        when(inputControl.isValidClient(anyLong())).thenReturn(true);
        bot.onStart(user, inputControl);
        verify(inputControl, times(1)).isValidClient(anyLong());
    }
    @Test
    public void onStartValidNotValid() {
        user.setState(UserState.START);
        user.setChatId(562373389L);
        InputControl inputControl = mock(InputControl.class);
        when(inputControl.isValidClient(anyLong())).thenReturn(false);
        bot.onStart(user, inputControl);
        verify(inputControl, times(1)).isValidClient(anyLong());
    }

    @Test
    public void onKeyValid() {
        DataBaseSql.connectSQLite();
        user.setState(UserState.KEY);
        user.setChatId(562373389L);
        InputControl inputControl = new InputControl();
        bot.onKey(user, inputControl);
        assertEquals(UserState.KEY, user.getState());
        assertEquals(562373389L, user.getChatId());
        assertTrue(inputControl.isValidClient(562373389L));
    }

    @SneakyThrows
    @Test
    public void onKeyNotValid() {
        DataBaseSql.connectSQLite();
        user.setState(UserState.KEY);
        user.setChatId(562373389L);
        InputControl inputControl = new InputControl();
        bot.onKey(user, inputControl);
        assertTrue(inputControl.isValidClient(user.getChatId()));
    }
}