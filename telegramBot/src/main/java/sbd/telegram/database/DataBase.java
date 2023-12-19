package sbd.telegram.database;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.*;

public class DataBase {
    private static final String url = "jdbc:sqlite:C:/Users/Danie/Desktop/insurance/insurancecompanydb";

    private static Connection connection;
    private static Statement statmt;
    private static ResultSet resSet;

    @SneakyThrows
    public static void connectDataBase() {
        // db parameters
        // create a connection to the database
//        connection = DriverManager.getConnection(url);
        System.out.println("Connection to SQLite has been established.");
//        if (connection != null) {
//            DatabaseMetaData meta = connection.getMetaData();
//            System.out.println("The driver name is " + meta.getDriverName());
//            System.out.println("A new database has been created.");
//        }
    }

    @SneakyThrows
    public static void createDataBase() {
//        statmt = connection.createStatement();
//        statmt.execute("CREATE TABLE if not exists 'users' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' text, 'phone' INT);");

        System.out.println("Таблица создана или уже существует.");
    }

    // --------Заполнение таблицы--------
    @SneakyThrows
    public static void writeTable()
    {
        String fullName = listOfInputData.get(0) + listOfInputData.get(1) + listOfInputData.get(2);
        String email = listOfInputData.get(3);
        String phoneNumber = listOfInputData.get(4);
        statmt.execute("INSERT INTO client ('clientId', 'fullName', 'email', 'phone') VALUES (123, fullName, email, phoneNumber); ");
//        statmt.execute("INSERT INTO 'users' ('name', 'phone') VALUES ('Masha', 456123); ");

        System.out.println("Таблица заполнена");
    }

    // -------- Вывод таблицы--------
    @SneakyThrows
    public static void readTable() {
//        resSet = statmt.executeQuery("SELECT * FROM client");

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
//        connection.close();
//        statmt.close();
//        resSet.close();

        System.out.println("Соединения закрыты");
    }
}
