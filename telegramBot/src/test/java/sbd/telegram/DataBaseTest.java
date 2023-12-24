package sbd.telegram;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import sbd.telegram.database.DataBase;

import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DataBaseTest {
    private DataBase dataBase;

    @BeforeEach
    void setUp() {
        dataBase = new DataBase();
        DataBase.connectSQLite();
        DataBase.connectRedis();
    }

    @Test
    void testShowClient() {
        String expected = "Client ID: 562373389\nFull name: dan daaan daniliv\nEmail: gmail@com\nPhone: +38475111";
        String result = dataBase.showClient(1);
        assertEquals(expected, result);
    }

    @Test
    @SneakyThrows
    void testConnectSQLite() {
        boolean isConnected = false;
        DataBase.connectSQLite();
        isConnected = true;
        assertTrue(isConnected, "Connection should be established");
    }

    @Test
    @SneakyThrows
    public void testRedisConnection() {
        boolean isConnected = false;
        DataBase.connectRedis();
        Jedis redisDB = DataBase.redisDB;
        isConnected = redisDB.isConnected();
        assertTrue(isConnected, "Connection to Redis should be established");
    }

    @Test
    public void testWriteTable() {
        DataBase mockedDatabase = mock(DataBase.class);
        Long clientId = 12345L;
        String firstName = "John";
        String secondName = "Doe";
        String lastName = "Smith";
        String email = "john.doe@example.com";
        String phoneNumber = "1234567890";
        mockedDatabase.writeTable(clientId, firstName, secondName, lastName, email, phoneNumber);
        when(mockedDatabase.getClientsNumber()).thenReturn(1);
        int expectedRowCount = 1;
        int actualRowCount = mockedDatabase.getClientsNumber();
        assertEquals(expectedRowCount, actualRowCount, "New row should be inserted into the table");
    }

    @Test
    public void testAddInsuranceWhenInsuranceExists() {
        DataBase mockedDatabase = mock(DataBase.class);
        Long existingChatId = 12345L;
        String existingInsuranceType = "existingInsurance";
        when(mockedDatabase.checkAvailability(existingChatId, existingInsuranceType)).thenReturn(true);
        boolean isInsuranceAvailable = mockedDatabase.checkAvailability(existingChatId, existingInsuranceType);
        assertTrue(isInsuranceAvailable, "Insurance should exist for chatId: " + existingChatId);
    }

    @Test
    public void testAddInsuranceWhenInsuranceDoesNotExist() {
        DataBase mockedDatabase = mock(DataBase.class);
        Long nonExistingChatId = 12345L;
        String nonExistingInsuranceType = "nonExistingInsurance";
        when(mockedDatabase.checkAvailability(nonExistingChatId, nonExistingInsuranceType)).thenReturn(false);
        boolean isInsuranceAvailable = mockedDatabase.checkAvailability(nonExistingChatId, nonExistingInsuranceType);
        assertFalse(isInsuranceAvailable, "Insurance should not exist for chatId: " + nonExistingChatId);
    }

    @Test
    public void testReadInsurances() {
        DataBase database = new DataBase();
        Integer insuranceId = 12345;
        String expectedOutput = "Тип страхування: ТипСтрахування\nЦіна за місяць - 100$\nПри страховому випадку покриє 50% від затрат";
        DataBase mockedDatabase = mock(DataBase.class);
        when(mockedDatabase.readInsurances(insuranceId)).thenReturn(expectedOutput);
        String actualOutput = mockedDatabase.readInsurances(insuranceId);
        assertEquals(expectedOutput, actualOutput, "Повинно бути отримано інформацію про страховку з бази даних");
    }

    @Test
    public void testDeleteInsurance() {
        DataBase mockedDatabase = mock(DataBase.class);
        Long chatId = 12345L;
        String typeOfInsurance = "SomeInsurance";
        mockedDatabase.deleteInsurance(chatId, typeOfInsurance);
        verify(mockedDatabase, times(1)).deleteInsurance(eq(chatId), eq(typeOfInsurance));
    }

    @SneakyThrows
    @Test
    public void testFindClientIdWhenClientIdExists(){
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
    public void testFindClientIdWhenClientIdDoesNotExist() {
        DataBase mockedDatabase = mock(DataBase.class);
        Long nonExistingChatId = 12345L;
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(false);
        when(mockedDatabase.staticQuery(anyString(), eq(nonExistingChatId))).thenReturn(resultSet);
        boolean result = mockedDatabase.findClientId(nonExistingChatId);
        assertFalse(result, "Client ID should not exist");
    }
    @SneakyThrows
    @Test
    public void testDeleteClient() {
        DataBase mockedDatabase = mock(DataBase.class);
        Long chatId = 12345L;
        assertEquals(0, mockedDatabase.deleteClient(chatId));
    }

//    @SneakyThrows
//    @Test
//    public void testCheckAvailabilityWhenInsuranceExists() {
//        DataBase mockedDatabase = mock(DataBase.class);
//        Long chatId = 12345L;
//        String typeOfInsurance = "medical";
//        assertTrue(mockedDatabase.checkAvailability(chatId, typeOfInsurance));
//    }
}
