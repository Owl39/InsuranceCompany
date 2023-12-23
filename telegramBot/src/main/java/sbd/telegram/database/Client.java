package sbd.telegram.database;

import lombok.SneakyThrows;

import java.util.ArrayList;

public class Client {
    private static String firstName;
    private static String secondName;
    private static String lastName;
    private static String mail;
    private static String phoneNumber;
    DataBase dataBase;

    public Client() {
        this.dataBase = new DataBase();
    }

    @SneakyThrows
    public ArrayList<String> stringParser(String inputText) {
        if (inputText.startsWith("/") || inputText.isEmpty())
            return null;
        String[] splitParts = inputText.split(" ");

        ArrayList<String> validValues = new ArrayList<>();
        for (String part : splitParts) {
            String values = part.replaceAll(" ", "");
            if (!values.trim().isEmpty())
                validValues.add(values.trim());
        }
        if (validValues.size() == 5) {
            secondName = validValues.get(0);
            firstName = validValues.get(1);
            lastName = validValues.get(2);
            mail = validValues.get(3);
            phoneNumber = validValues.get(4);
            return validValues;
        } else
            return null;
    }

    public void writeClientTable(Long chatId) {
        if ((!dataBase.findClientId(chatId)) && (firstName != null)) {
            dataBase.writeTable(chatId, firstName, secondName, lastName, mail, phoneNumber);
            firstName = null;
            secondName = null;
            lastName = null;
            mail = null;
            phoneNumber = null;
        }
    }

    public boolean isValidClient(Long chatId){
        return dataBase.findClientId(chatId);
    }
}
