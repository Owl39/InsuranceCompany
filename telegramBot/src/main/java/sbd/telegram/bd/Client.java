package sbd.telegram.bd;

import lombok.SneakyThrows;

import java.util.ArrayList;

public class Client {
    // ПАРСЕР ДЛЯ ВВОДА ФИО
    @SneakyThrows
    public ArrayList<String> createRegistration(String inputText) {
        if (inputText.startsWith("/") || inputText.isEmpty())
            return null;
        String[] splitParts = inputText.split(" ");

        ArrayList<String> validValues = new ArrayList<>();

        for (String part : splitParts) {
            String values = part.replaceAll(" ", "");
            if (!values.trim().isEmpty())
                validValues.add(values.trim());
        }
        return validValues;
    }
}
