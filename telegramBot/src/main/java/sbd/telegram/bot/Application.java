package sbd.telegram.bot;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import sbd.telegram.database.DataBase;

public class Application {
    @SneakyThrows
    public static void main(String[] args) {
//        DataBase.connectDataBase();
//        DataBase.createDataBase();

        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new Bot());

//        DataBase.writeTable();
//        DataBase.readTable();
//        DataBase.closeDataBase();
    }
}