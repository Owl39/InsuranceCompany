package sbd.telegram.database;

import lombok.SneakyThrows;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataBaseRedis {
    public static Jedis redisDB;

    @SneakyThrows
    public static void connectRedis() {
        redisDB = new Jedis("redis-14638.c55.eu-central-1-1.ec2.cloud.redislabs.com", 14638);
        redisDB.auth("iphJnYUL7vUVvzySufTv2fDvrpSLsdMv");
    }

    @SneakyThrows
    public boolean isAdmin(Long chatId) {
        return redisDB.exists("worker:" + chatId);
    }


    @SneakyThrows
    public String showWorker(String key) {
        Map<String, String> workerInfo = redisDB.hgetAll(key);
        return ("Worker ID: " + key.substring(key.lastIndexOf(":") + 1) + "\nFirstname: " + workerInfo.get("firstname") + "\nLastname: " +
                workerInfo.get("lastname") + "\nPosition: " + workerInfo.get("position") + "\nPhone: " + workerInfo.get("phone") + "\nSalary: " + workerInfo.get("salary"));
    }

    @SneakyThrows
    public String[] sortWorkersBySalary(Set<String> keys) {
        List<String> keysList = new ArrayList<>(keys);
        keysList.sort((k1, k2) -> {
            Double salary1 = Double.parseDouble(redisDB.hget(k1, "salary"));
            Double salary2 = Double.parseDouble(redisDB.hget(k2, "salary"));
            return salary2.compareTo(salary1);
        });
        return keysList.toArray(new String[0]);
    }
}
