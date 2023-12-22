package sbd.telegram.database;

import lombok.SneakyThrows;

import java.sql.*;

public class DataBase {
    private static final String url = "jdbc:sqlite:C:/Users/Stas/Documents/!DZ/СБД/InsuranceCompany/insurancecompanydb";
    private static Connection connection;
    private Statement statmt;
    private ResultSet resSet;
    private String resultString = null;
    private String query = null;

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
    public void writeTable(Long clientId, String firstName, String secondName, String lastName, String email, String phoneNumber) {
        statmt = connection.createStatement();
        String fullName = secondName + " " + firstName + " " + lastName;
        statmt.execute("INSERT INTO client (clientId, fullName, email, phone) VALUES ('" + clientId + "', '" + fullName + "', '" + email +
                "', '" + phoneNumber + "')");
    }

    // -------- Вывод таблицы--------
    @SneakyThrows
    public void readTable(Long chatId) {
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

//            bot.execute(bot.printText("Full name = " + fullName + "\nEmail = " + email + "\nPhone = " + phone));
//            TODO return
        }
    }

    @SneakyThrows
    public String readInsurances(Integer insuranceId) {
        query = "SELECT insuranceType, monthlyPrice, payoutPercentage FROM insurances WHERE insuranceId = ?";
        resSet = staticQuerySetInsuranceId(query, insuranceId);
        while (resSet.next()) {
            String insuranceType = resSet.getString("insuranceType");
            String monthlyPrice = resSet.getString("monthlyPrice");
            String payoutPercentage = resSet.getString("payoutPercentage");

            resultString = ("Тип страхування: " + insuranceType + "\nЦіна за місяць - " + monthlyPrice + "$" + "\nПри страховому випадку покриє " + payoutPercentage + "% від затрат");
        }
        return resultString;
    }

    @SneakyThrows
    public int deleteClient(Long chatId) {
        query = "DELETE FROM client WHERE clientId = ?";
        return staticUpdate(query, chatId);
    }

    @SneakyThrows
    public void addInsurance(Long chatId, String typeOfInsurance) {
//        TODO сделать проверку на наличие страховки
        query = "INSERT INTO  " + typeOfInsurance + " (clientId, fullName, email, phone) SELECT client.clientID, client.fullName, client.email, client.phone FROM client WHERE client.clientId = ?";
        staticUpdate(query, chatId);
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
