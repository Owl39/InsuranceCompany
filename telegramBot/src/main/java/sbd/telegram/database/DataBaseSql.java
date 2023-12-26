package sbd.telegram.database;

import lombok.SneakyThrows;

import java.sql.*;

public class DataBaseSql {
    private static final String url = "jdbc:sqlite:C:/Users/Stas/Documents/!DZ/СБД/InsuranceCompany/insurancecompanydb";
    private static Connection connection;
    private ResultSet resSet;
    private String resultString = null;

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
    public void writeTable(Long clientId, String firstName, String secondName, String lastName, String email, String phoneNumber) {
        Statement statmt = connection.createStatement();
        String fullName = secondName + " " + firstName + " " + lastName;
        statmt.execute("INSERT INTO client (clientId, fullName, email, phone) VALUES ('" + clientId + "', '" + fullName + "', '" + email +
                "', '" + phoneNumber + "')");
    }

    public void addInsurance(Long chatId, String typeOfInsurance) {
        String query = "INSERT INTO  " + typeOfInsurance + " (clientId, fullName, email, phone) SELECT client.clientID, client.fullName, client.email, client.phone FROM client WHERE client.clientId = ?";
        staticUpdate(query, chatId);
    }

    @SneakyThrows
    public String readInsurances(Integer insuranceId) {
        String query = "SELECT insuranceType, monthlyPrice, payoutPercentage FROM insurances WHERE insuranceId = ?";
        resSet = staticQuerySetInsuranceId(query, insuranceId);
        while (resSet.next()) {
            String insuranceType = resSet.getString("insuranceType");
            String monthlyPrice = resSet.getString("monthlyPrice");
            String payoutPercentage = resSet.getString("payoutPercentage");
            resultString = ("Тип страхування: " + insuranceType + "\nЦіна за місяць - " + monthlyPrice + "$" + "\nПри страховому випадку покриє " + payoutPercentage + "% від затрат");
        }
        return resultString;
    }

    public void deleteInsurance(Long chatId, String typeOfInsurance) {
        String query = "DELETE FROM " + typeOfInsurance + " WHERE clientId = ?";
        staticUpdate(query, chatId);
    }

    @SneakyThrows
    public boolean findClientId(Long chatId) {
        String query = "SELECT * FROM client WHERE clientId = ?";
        resSet = staticQuery(query, chatId);
        return resSet.next();
    }

    public int deleteClient(Long chatId) {
        String query = "DELETE FROM client WHERE clientId = ?";
        return staticUpdate(query, chatId);
    }

    @SneakyThrows
    public boolean checkAvailability(Long chatId, String typeOfInsurance) {
        String query = "SELECT 1 FROM " + typeOfInsurance + " WHERE clientId = ?";
        resSet = staticQuery(query, chatId);
        return resSet.next();
    }

    @SneakyThrows
    public int getClientsNumber(String tableName) {
        String query = "SELECT COUNT(*) FROM " + tableName;
        PreparedStatement statmt = connection.prepareStatement(query);
        resSet = statmt.executeQuery();
        resSet.next();
        return resSet.getInt(1);
    }

    @SneakyThrows
    public String showClient(int i) {
        String query = "SELECT * FROM client";
        PreparedStatement statmt = connection.prepareStatement(query);
        resSet = statmt.executeQuery();
        while (i > 0) {
            resSet.next();
            --i;
        }
        return ("Client ID: " + resSet.getInt("clientId") + "\nFull name: " + resSet.getString("fullName") + "\nEmail: " +
                resSet.getString("email") + "\nPhone: " + resSet.getString("phone"));
    }


    @SneakyThrows
    public int getProfitability(String tableName) {
        String query = "SELECT monthlyPrice FROM insurances WHERE insuranceType = ?";
        resSet = staticQuerySetInsuranceType(query, tableName);
        resSet.next();
        return resSet.getInt("monthlyPrice");
    }

    @SneakyThrows
    public ResultSet staticQuery(String query, Long chatId) {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setLong(1, chatId);
        return preparedStatement.executeQuery();
    }

    @SneakyThrows
    public int staticUpdate(String query, Long chatId) {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setLong(1, chatId);
        return preparedStatement.executeUpdate();
    }

    @SneakyThrows
    public ResultSet staticQuerySetInsuranceId(String query, int insuranceId) {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, insuranceId);
        return preparedStatement.executeQuery();
    }

    @SneakyThrows
    public ResultSet staticQuerySetInsuranceType(String query, String tableName) {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, tableName);
        return preparedStatement.executeQuery();
    }
}
