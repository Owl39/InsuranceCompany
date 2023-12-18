package sbd.telegram;

import lombok.SneakyThrows;

import java.util.ArrayList;

public class Client extends Bot {
    //    TODO переделать так, чтобы пользователь мог вводить данные
    //          Либо убрать возможность нажимать кнопки
    @SneakyThrows
    public void createRegistration() {
//        ПАРСЕР ДЛЯ ВВОДА ФИО

        String inputText = "Cветличный; Станислав; Сергеевич; 1234+;";
        String[] splitParts = inputText.split(";");

        ArrayList<String> validValues = new ArrayList<>();

        for (String part : splitParts) {
            String values = part.replaceAll("[^\\p{L} -]", "");
            if (!values.trim().isEmpty())
                validValues.add(values.trim());
        }
        int count = 0;
        execute(printText("Ваше ФИО:"));
        for (String fio : validValues) {
            count++;
            execute(printText(count + ": " + fio));
        }
    }
}
