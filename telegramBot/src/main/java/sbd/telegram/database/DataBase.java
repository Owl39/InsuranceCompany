package sbd.telegram.database;

import lombok.SneakyThrows;

import java.sql.*;

public class DataBase {
    private static final String filePath = "/Users/Stas/Documents/!DZ/СБД/InsuranceCompany/";
    private static final String fileName = "insurancecompanydb";
    private static final String url = "jdbc:sqlite:C:" + filePath + fileName;
    private static Connection connection;
    private static Statement statmt;
    private static ResultSet resSet;

    @SneakyThrows
    public static void connectDataBase() {
        // db parameters
        // create a connection to the database
        connection = DriverManager.getConnection(url);
        System.out.println("Connection to SQLite has been established.");
//        if (connection != null) {
//            DatabaseMetaData meta = connection.getMetaData();
//            System.out.println("The driver name is " + meta.getDriverName());
//            System.out.println("A new database has been created.");
//        }
    }

    @SneakyThrows
    public static void createDataBase() {
        statmt = connection.createStatement();
//        statmt.execute("CREATE TABLE if not exists 'users' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' text, 'phone' INT);");

        System.out.println("Таблица создана или уже существует.");
    }

    // --------Заполнение таблицы--------
    public static void writeDataBase() {
//        statmt.execute("INSERT INTO 'users' ('name', 'phone') VALUES ('Petya', 125453); ");
//        statmt.execute("INSERT INTO 'users' ('name', 'phone') VALUES ('Vasya', 321789); ");
//        statmt.execute("INSERT INTO 'users' ('name', 'phone') VALUES ('Masha', 456123); ");

        System.out.println("Таблица заполнена");
    }

    // -------- Вывод таблицы--------
    @SneakyThrows
    public static void readDataBase() {
//        resSet = statmt.executeQuery("SELECT * FROM users");

//        while(resSet.next())
//        {
//            int id = resSet.getInt("id");
//            String  name = resSet.getString("name");
//            String  phone = resSet.getString("phone");
//            System.out.println( "ID = " + id );
//            System.out.println( "name = " + name );
//            System.out.println( "phone = " + phone );
//            System.out.println();
//        }

        System.out.println("Таблица выведена");
    }

    @SneakyThrows
    public static void closeDataBase() {
        connection.close();
        statmt.close();
        resSet.close();

        System.out.println("Соединения закрыты");
    }
}
