package sbd.telegram.database;

import lombok.SneakyThrows;
import sbd.telegram.bot.Bot;

import java.sql.*;

public class DataBase {
    private static final String url = "jdbc:sqlite:C:/Users/Stas/Documents/!DZ/СБД/InsuranceCompany/insurancecompanydb";
    private static Connection connection;
    private static Statement statmt;
    private static ResultSet resSet;

    @SneakyThrows
    public static void connectDataBase() {
        connection = DriverManager.getConnection(url);
        System.out.println("Connection to SQLite has been established.");
        if (connection != null) {
            DatabaseMetaData meta = connection.getMetaData();
            System.out.println("The driver name is " + meta.getDriverName());
            System.out.println("A new database has been created.");
        }
    }

    // --------Заполнение таблицы--------
    @SneakyThrows
    public static void writeTable(Long clientId, String firstName, String secondName, String lastName, String email, String phoneNumber) {
        statmt = connection.createStatement();
        String fullName = secondName + " " + firstName + " " + lastName;
        statmt.execute("INSERT INTO client (clientId, fullName, email, phone) VALUES ('" + clientId + "', '" + fullName + "', '" + email +
                "', '" + phoneNumber + "')");
        System.out.println("Таблица заполнена");
    }

    @SneakyThrows
    public static int deleteClient(Long chatId) {
        String query = "DELETE FROM client WHERE clientId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setLong(1, chatId);
        return preparedStatement.executeUpdate();
    }


    // -------- Вывод таблицы--------
    @SneakyThrows
    public static void readTable(Long chatId) {
        Bot bot = new Bot();
//        resSet = statmt.executeQuery("SELECT * FROM client");
//        System.out.println("\n\n");
//        while (resSet.next()) {
//            int clientId = resSet.getInt("clientId");
//            String fullName = resSet.getString("fullName");
//            String email = resSet.getString("email");
//            String phone = resSet.getString("phone");
//        }
//       TODO Вывод для клиента
        String query = "SELECT fullName, email, phone FROM client WHERE clientId = ?";
        resSet = staticQuery(query, chatId);
        while (resSet.next()) {
            String fullName = resSet.getString("fullName");
            String email = resSet.getString("email");
            String phone = resSet.getString("phone");

            bot.execute(bot.printText("Full name = " + fullName));
            bot.execute(bot.printText("Email = " + email));
            bot.execute(bot.printText("Phone = " + phone));
        }
        System.out.println("\nТаблица выведена\n");
    }

    @SneakyThrows
    public static boolean findClientId(Long chatId) {
        String query = "SELECT * FROM client WHERE clientId = ?";
        resSet = staticQuery(query, chatId);
        return resSet.next();
    }

    @SneakyThrows
    private static ResultSet staticQuery(String query, Long chatId) {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setLong(1, chatId);
        return preparedStatement.executeQuery();
    }
}
