package sbd.telegram.database;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import sbd.telegram.database.DataBase;

import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataBaseTest {
    private DataBase dataBase;

    @BeforeEach
    void setUp() {
        dataBase = new DataBase();
        DataBase.connectSQLite();
        DataBase.connectRedis();
    }

    @Test
    void showClientTest() {
        String expected = "Client ID: 562373389\nFull name: dan daaan daniliv\nEmail: gmail@com\nPhone: +38475111";
        String result = dataBase.showClient(1);
        assertEquals(expected, result);
    }

    @Test
    @SneakyThrows
    void connectSQLiteTest() {
        boolean isConnected = false;
        DataBase.connectSQLite();
        isConnected = true;
        assertTrue(isConnected, "Connection should be established");
    }

    @Test
    @SneakyThrows
    public void redisConnectionTest() {
        boolean isConnected = false;
        DataBase.connectRedis();
        Jedis redisDB = DataBase.redisDB;
        isConnected = redisDB.isConnected();
        assertTrue(isConnected, "Connection to Redis should be established");
    }

    @Test
    public void writeTableTest() {
        DataBase mockedDatabase = mock(DataBase.class);
        Long clientId = 12345L;
        String firstName = "John";
        String secondName = "Doe";
        String lastName = "Smith";
        String email = "john.doe@example.com";
        String phoneNumber = "1234567890";
        mockedDatabase.writeTable(clientId, firstName, secondName, lastName, email, phoneNumber);
        when(mockedDatabase.getClientsNumber("client")).thenReturn(1);
        int expectedRowCount = 1;
        int actualRowCount = mockedDatabase.getClientsNumber("client");
        assertEquals(expectedRowCount, actualRowCount, "New row should be inserted into the table");
    }

    @Test
    public void addInsuranceValidTest() {
        DataBase mockedDatabase = mock(DataBase.class);
        Long existingChatId = 12345L;
        String existingInsuranceType = "existingInsurance";
        when(mockedDatabase.checkAvailability(existingChatId, existingInsuranceType)).thenReturn(true);
        boolean isInsuranceAvailable = mockedDatabase.checkAvailability(existingChatId, existingInsuranceType);
        assertTrue(isInsuranceAvailable, "Insurance should exist for chatId: " + existingChatId);
    }

    @Test
    public void addInsuranceNotValidTest() {
        DataBase mockedDatabase = mock(DataBase.class);
        Long nonExistingChatId = 12345L;
        String nonExistingInsuranceType = "nonExistingInsurance";
        when(mockedDatabase.checkAvailability(nonExistingChatId, nonExistingInsuranceType)).thenReturn(false);
        boolean isInsuranceAvailable = mockedDatabase.checkAvailability(nonExistingChatId, nonExistingInsuranceType);
        assertFalse(isInsuranceAvailable, "Insurance should not exist for chatId: " + nonExistingChatId);
    }

    @Test
    public void readInsurancesTest() {
        DataBase database = new DataBase();
        Integer insuranceId = 12345;
        String expectedOutput = "Тип страхування: ТипСтрахування\nЦіна за місяць - 100$\nПри страховому випадку покриє 50% від затрат";
        DataBase mockedDatabase = mock(DataBase.class);
        when(mockedDatabase.readInsurances(insuranceId)).thenReturn(expectedOutput);
        String actualOutput = mockedDatabase.readInsurances(insuranceId);
        assertEquals(expectedOutput, actualOutput, "Повинно бути отримано інформацію про страховку з бази даних");
    }

    @Test
    public void deleteInsuranceTest() {
        DataBase mockedDatabase = mock(DataBase.class);
        Long chatId = 12345L;
        String typeOfInsurance = "SomeInsurance";
        mockedDatabase.deleteInsurance(chatId, typeOfInsurance);
        verify(mockedDatabase, times(1)).deleteInsurance(eq(chatId), eq(typeOfInsurance));
    }

    @SneakyThrows
    @Test
    public void findClientIdValidTest(){
        DataBase mockedDatabase = mock(DataBase.class);
        Long existingChatId = 12345L;
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true);
        when(mockedDatabase.staticQuery(anyString(), eq(existingChatId))).thenReturn(resultSet);
        boolean result = mockedDatabase.findClientId(existingChatId);
        assertFalse(result, "Client ID should exist");
    }

    @SneakyThrows
    @Test
    public void findClientIdNotValidTest() {
        Long nonExistingChatId = 54321L;
        boolean result = dataBase.findClientId(nonExistingChatId);
        assertFalse(result, "Client ID should not exist");
    }
    @SneakyThrows
    @Test
    public void deleteClientTest() {
        DataBase mockedDatabase = mock(DataBase.class);
        Long chatId = 12345L;
        assertEquals(0, mockedDatabase.deleteClient(chatId));
    }

    @SneakyThrows
    @Test
    public void checkAvailabilityValidTest() {
        Long chatId = 12345L;
        String typeOfInsurance = "car";
        assertTrue(!dataBase.checkAvailability(chatId, typeOfInsurance));
    }
    @SneakyThrows
    @Test
    public void checkAvailabilityNotValidTest() {
        Long chatId = 12345L;
        String typeOfInsurance = "medical";
        assertFalse(dataBase.checkAvailability(chatId, typeOfInsurance));
    }
    @SneakyThrows
    @Test
    public void isAdminTest() {
        Long chatId = 562373389L;
        assertTrue(dataBase.isAdmin(chatId));
    }

    @SneakyThrows
    @Test
    public void isNotAdminTest() {
        Long chatId = 12345L;
        assertFalse(dataBase.isAdmin(chatId));
    }

    @SneakyThrows
    @Test
    public void showWorkerTest() {
        String key = "worker:5";
        String expectedOutput = "Worker ID: 5\nFirstname: Al\nLastname: Capone\nPosition: consultant\nPhone: +380639485789\nSalary: 1950";
                String actualOutput = dataBase.showWorker(key);
        assertEquals(expectedOutput, actualOutput);
    }

    @SneakyThrows
    @Test
    public void getProfitabilityTest() {
        String tableName = "life";
        int actualOutput = dataBase.getProfitability(tableName);
        assertEquals(20, actualOutput);
    }
}
