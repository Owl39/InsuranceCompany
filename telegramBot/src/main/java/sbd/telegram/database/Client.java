package sbd.telegram.database;

import lombok.SneakyThrows;
import sbd.telegram.bot.Bot;
import sbd.telegram.bot.ButtonsActions;

import java.util.ArrayList;

public class Client {
    private static String firstName;
    private static String secondName;
    private static String lastName;
    private static String mail;
    private static String phoneNumber;
    private DataBase dataBase;
    private Bot bot;
    private ButtonsActions buttonsActions;

    public Client() {
        this.dataBase = new DataBase();
        this.bot = new Bot();
//        this.bot = new Bot(new ButtonsActions(null, dataBase, this));
//        (new ButtonsActions(this.dataBase));
//        this.buttonsActions = new ButtonsActions(bot, dataBase, this);
//         // Передача Client и ButtonsActions в Bot
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
