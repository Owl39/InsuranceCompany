package sbd.telegram.database;

import lombok.SneakyThrows;
import redis.clients.jedis.Jedis;

import java.util.Map;

import java.sql.*;

public class DataBase {
    private static final String url = "jdbc:sqlite:C:/Users/Stas/Documents/!DZ/СБД/InsuranceCompany/insurancecompanydb";
    private static Connection connection;
    public static Jedis redisDB;
    private ResultSet resSet;
    private String resultString = null;
    private String query = null;
    private final Insurance insurance;

    public DataBase() {
        this.insurance = new Insurance();
    }

    @SneakyThrows
    public static void connectSQLite() {
        connection = DriverManager.getConnection(url);
        System.out.println("Connection to SQLite has been established.");
        if (connection != null) {
            DatabaseMetaData meta = connection.getMetaData();
            System.out.println("The driver name is " + meta.getDriverName());
            System.out.println("A new database has been created.");
        }
    }

    @SneakyThrows
    public static void connectRedis() {
        redisDB = new Jedis("redis-14638.c55.eu-central-1-1.ec2.cloud.redislabs.com", 14638);
        redisDB.auth("iphJnYUL7vUVvzySufTv2fDvrpSLsdMv");
    }

    // --------Заполнение таблицы--------
    @SneakyThrows
    public void writeTable(Long clientId, String firstName, String secondName, String lastName, String email, String phoneNumber) {
        Statement statmt = connection.createStatement();
        String fullName = secondName + " " + firstName + " " + lastName;
        statmt.execute("INSERT INTO client (clientId, fullName, email, phone) VALUES ('" + clientId + "', '" + fullName + "', '" + email +
                "', '" + phoneNumber + "')");
    }

    // -------- Вывод таблицы--------
    @SneakyThrows
    public void readTable(Long chatId) {
//       TODO Вывод для клиента
        String query = "SELECT fullName, email, phone FROM client WHERE clientId = ?";
        resSet = staticQuery(query, chatId);
        while (resSet.next()) {
            String fullName = resSet.getString("fullName");
            String email = resSet.getString("email");
            String phone = resSet.getString("phone");

//            bot.execute(bot.printText("Full name = " + fullName + "\nEmail = " + email + "\nPhone = " + phone));
//            TODO return
        }
    }
//
//    @SneakyThrows
//    public void getOneClient(Long chatId) {
//        resSet = statmt.executeQuery("SELECT * FROM client");
//        while (resSet.next()) {
//            int clientId = resSet.getInt("clientId");
//            String fullName = resSet.getString("fullName");
//            String email = resSet.getString("email");
//            String phone = resSet.getString("phone");
//        }
//    }

    public void addInsurance(Long chatId, String typeOfInsurance) {
//        TODO сделать проверку на наличие страховки
        query = "INSERT INTO  " + typeOfInsurance + " (clientId, fullName, email, phone) SELECT client.clientID, client.fullName, client.email, client.phone FROM client WHERE client.clientId = ?";
        staticUpdate(query, chatId);
    }

    @SneakyThrows
    public String readInsurances(Integer insuranceId) {
        query = "SELECT insuranceType, monthlyPrice, payoutPercentage FROM insurances WHERE insuranceId = ?";
        resSet = staticQuerySetInsuranceId(query, insuranceId);
        while (resSet.next()) {
            insurance.setInsuranceType(resSet.getString("insuranceType"));
            insurance.setPayoutPercentage(resSet.getString("monthlyPrice"));
            insurance.setMonthlyPrice(resSet.getString("payoutPercentage"));
            resultString = ("Тип страхування: " + insurance.getInsuranceType() + "\nЦіна за місяць - " + insurance.getMonthlyPrice() + "$" + "\nПри страховому випадку покриє " + insurance.getPayoutPercentage() + "% від затрат");
        }
        return resultString;
    }

    public void deleteInsurance(Long chatId, String typeOfInsurance) {
        query = "DELETE FROM " + typeOfInsurance + " WHERE clientId = ?";
        staticUpdate(query, chatId);
    }

    @SneakyThrows
    public boolean findClientId(Long chatId) {
        query = "SELECT * FROM client WHERE clientId = ?";
        resSet = staticQuery(query, chatId);
        return resSet.next();
    }

    public int deleteClient(Long chatId) {
        query = "DELETE FROM client WHERE clientId = ?";
        return staticUpdate(query, chatId);
    }

    @SneakyThrows
    public boolean checkAvailability(Long chatId, String typeOfInsurance) {
        query = "SELECT 1 FROM " + typeOfInsurance + " WHERE clientId = ?";
        resSet = staticQuery(query, chatId);
        return resSet.next();
    }

    @SneakyThrows
    public boolean isAdmin(Long chatId) {
        return redisDB.exists("worker:" + chatId);
    }

    @SneakyThrows
    public int getClientsNumber() {
        query = "SELECT COUNT(*) FROM client";
        PreparedStatement statmt = connection.prepareStatement(query);
        resSet = statmt.executeQuery();
        resSet.next();
        return resSet.getInt(1);
    }

    @SneakyThrows
    public String showWorker(String key) {
        Map<String, String> workerInfo = redisDB.hgetAll(key);
        return resultString = ("Worker ID: " + key.substring(key.lastIndexOf(":") + 1) + "\nFirstname: " + workerInfo.get("firstname") + "\nLastname: " +
                workerInfo.get("lastname") + "\nPosition: " + workerInfo.get("position") + "\nPhone: " + workerInfo.get("phone"));
    }

    @SneakyThrows
    public String showClient() {
        query = "SELECT * FROM client";
        PreparedStatement statmt = connection.prepareStatement(query);
        resSet = statmt.executeQuery();

//        while (resSet.next()) {
////            String clientId
//
//        }
        resSet.next();
        return ("Client ID: " + resSet.getInt("clientId") + "\nFull name: " + resSet.getString("fullName") + "\nEmail: " +
                resSet.getString("email") + "\nPhone: " + resSet.getString("phone"));
    }

    @SneakyThrows
    private ResultSet staticQuery(String query, Long chatId) {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setLong(1, chatId);
        return preparedStatement.executeQuery();
    }

    @SneakyThrows
    private int staticUpdate(String query, Long chatId) {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setLong(1, chatId);
        return preparedStatement.executeUpdate();
    }

    @SneakyThrows
    private ResultSet staticQuerySetInsuranceId(String query, int insuranceId) {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, insuranceId);
        return preparedStatement.executeQuery();
    }
}
