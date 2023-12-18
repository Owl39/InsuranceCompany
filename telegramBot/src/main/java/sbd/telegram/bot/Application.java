package sbd.telegram.bot;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import sbd.telegram.database.DataBase;
public class Application {
    @SneakyThrows
    public static void main(String[] args) {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new Bot());

        DataBase.connectDataBase();
        DataBase.createDataBase();
        DataBase.writeDataBase();
        DataBase.readDataBase();
        DataBase.closeDataBase();
    }
}