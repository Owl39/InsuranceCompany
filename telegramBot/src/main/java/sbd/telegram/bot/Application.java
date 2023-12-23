package sbd.telegram.bot;
import redis.clients.jedis.Jedis;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import sbd.telegram.database.DataBase;

public class Application {
    @SneakyThrows
    public static void main(String[] args) {
        DataBase.connectSQLite();
        DataBase.connectRedis();
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new Bot());
    }
}