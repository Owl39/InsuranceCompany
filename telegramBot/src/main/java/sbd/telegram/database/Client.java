package sbd.telegram.database;

import lombok.SneakyThrows;

import java.io.Serializable;
import java.util.ArrayList;

public class Client {
//    public static String firstName;
    private static String secondName;
    private static String lastName;
    private static String mail;
    private static String phoneNumber;
    private static String firstName;
    DataBase dataBase;
    public Client() {
        this.dataBase = new DataBase();
    }

    public Serializable inputStringParser(String inputText) {
        ArrayList<InputState> clientsInputs = new ArrayList<>();
        if (inputText.startsWith("/") || inputText.isEmpty()) {
            clientsInputs.add(InputState.NONE);
            return clientsInputs;
        }

        String[] splitParts = inputText.split(" ");
        ArrayList<String> validValues = new ArrayList<>();

        for (String part : splitParts) {
            String values = part.replaceAll(" ", "");
            if (!values.trim().isEmpty())
                validValues.add(values.trim());
        }

        if (validValues.size() == 5) {
            secondName = validValues.get(0);
            if (!secondName.matches("[a-zA-ZА-Яа-яіІїЇєЄґҐ]+"))
                clientsInputs.add(InputState.SECOND);

            firstName = validValues.get(1);
            if (!firstName.matches("[a-zA-ZА-Яа-яіІїЇєЄґҐ]+"))
                clientsInputs.add(InputState.FIRST);

            lastName = validValues.get(2);
            if (!lastName.matches("[a-zA-ZА-Яа-яіІїЇєЄґҐ]+"))
                clientsInputs.add(InputState.LAST);

            mail = validValues.get(3);
            if (!mail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))
                clientsInputs.add(InputState.EMAIL);

            phoneNumber = validValues.get(4);
            if (!phoneNumber.matches("^\\+\\d+$"))
                clientsInputs.add(InputState.PHONE_NUMBER);

            if (!clientsInputs.isEmpty()) {
                clientsInputs.add(InputState.NONE);
                return clientsInputs;
            } else
                return validValues;
        } else {
            clientsInputs.add(InputState.NONE);
            return clientsInputs;
        }
    }

    public void writeClientTable(Long chatId) {
        if ((!dataBase.findClientId(chatId)) && (firstName != null))
            dataBase.writeTable(chatId, firstName, secondName, lastName, mail, phoneNumber);
    }

    public boolean isValidClient(Long chatId) {
        return dataBase.findClientId(chatId);
    }
}
