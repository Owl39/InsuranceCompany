package sbd.telegram.bot;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import sbd.telegram.database.DataBaseRedis;
import sbd.telegram.database.DataBaseSql;

public class Application {
    @SneakyThrows
    public static void main(String[] args) {
        DataBaseSql.connectSQLite();
        DataBaseRedis.connectRedis();
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new Bot());
    }
}