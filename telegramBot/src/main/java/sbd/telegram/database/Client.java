package sbd.telegram.database;

import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.Collections;

public class Client {
    // ПАРСЕР ДЛЯ ВВОДА ФИО
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

        // Сдвигаем validValues на одну позицию вправо
//        Collections.rotate(validValues, 1);

        return validValues;
    }

}
