package sbd.telegram.database;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.sql.*;

public class DataBase {
    private static final String url = "jdbc:sqlite:C:/Users/Danie/Desktop/courseInsurance/insurancecompanydb";

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
    @SneakyThrows
    public static void writeTable() {
 //       statmt.execute("INSERT INTO client ('clientId', 'fullName', 'email', 'phone') VALUES (123, 'Danylo', 'fff@gmail.com', '+38099'); ");
//        statmt.execute("INSERT INTO 'users' ('name', 'phone') VALUES ('Vasya', 321789); ");
//        statmt.execute("INSERT INTO 'users' ('name', 'phone') VALUES ('Masha', 456123); ");

        System.out.println("Таблица заполнена");
    }

    // -------- Вывод таблицы--------
    @SneakyThrows
    public static void readTable() {
        resSet = statmt.executeQuery("SELECT * FROM client");

       while(resSet.next())
        {
            int clientId = resSet.getInt("clientId");
         //   String  name = resSet.getString("name");
          //  String  phone = resSet.getString("phone");
            System.out.println( "ID = " + clientId );
          //  System.out.println( "name = " + name );
          //  System.out.println( "phone = " + phone );
          //  System.out.println();
        }

        System.out.println("Таблица выведена");
    }

    @SneakyThrows
    public static void closeDataBase() {
        connection.close();
//        statmt.close();
//        resSet.close();

        System.out.println("Соединения закрыты");
    }
}
