package sbd.telegram.database;

import lombok.SneakyThrows;

import java.util.ArrayList;

public class Client {
    private static Long clientId;
    private static String firstName;
    private static String secondName;
    private static String lastName;
    private static String mail;
    private static String phoneNumber;

    @SneakyThrows
    public ArrayList<String> stringParser(String inputText, Long chatId) {
        if (inputText.startsWith("/") || inputText.isEmpty())
            return null;
        String[] splitParts = inputText.split(" ");

        ArrayList<String> validValues = new ArrayList<>();
        validValues.add(String.valueOf(chatId)); // Добавляем Long chatId в начало
        for (String part : splitParts) {
            String values = part.replaceAll(" ", "");
            if (!values.trim().isEmpty())
                validValues.add(values.trim());
        }
        if (validValues.size() == 6) {
            clientId = Long.valueOf(validValues.get(0));
            firstName = validValues.get(1);
            secondName = validValues.get(2);
            lastName = validValues.get(3);
            mail = validValues.get(4);
            phoneNumber = validValues.get(5);

            return validValues;
        } else
            return null;
    }

    public void clientTable(Long chatId) {
        if (!DataBase.findeClientId(chatId))
            DataBase.writeTable(clientId, firstName, secondName, lastName, mail, phoneNumber);
        DataBase.findeClientId(chatId);
    }
}
