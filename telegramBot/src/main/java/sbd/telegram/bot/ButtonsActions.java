package sbd.telegram.bot;

import lombok.SneakyThrows;
import sbd.telegram.controllers.User;
import sbd.telegram.database.Client;
import sbd.telegram.database.DataBase;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import static sbd.telegram.controllers.UserState.*;

public class ButtonsActions {

    private final Bot bot;
    private final Client client;
    private final DataBase dataBase;
    private final InlineKeyboard inlineKeyboard;
    static String typeName;
    public final ArrayList<String> arrayOfTypes = new ArrayList<>(Arrays.asList("Car", "Medical", "Life", "RealEstate", "Business"));
    public static ArrayList<String> arrayOfActiveIns = new ArrayList<>();

    public ButtonsActions() {
        this.bot = new Bot();
        this.dataBase = new DataBase();
        this.client = new Client();
        this.inlineKeyboard = new InlineKeyboard();
    }

    @SneakyThrows
    void doKeyAction(String dataText, Long chatId, User user) {
        switch (dataText) {
            case "Змінити данні акаунту":
//                TODO сделать запрос к бд на замену конкретных данных
                if (client.isValidClient(chatId)) {
                    user.setState(EDIT);
                    bot.execute(inlineKeyboard.buttonsForEditUser(chatId));
                    flipNoneToKey(user);
                } else noRegistratedClient(chatId);
                break;
            case "Оформити страхування":
                if (client.isValidClient(chatId)) {
                    user.setState(POLICY);
                    bot.execute(inlineKeyboard.buttonsForInsurance(chatId, arrayOfTypes));
                    flipNoneToKey(user);
                } else noRegistratedClient(chatId);
                break;
            case "Переглянути активні страхування":
                if (client.isValidClient(chatId)) {
                    user.setState(POLICY_CHECK);
                    showActiveInsurances(inlineKeyboard, user, chatId);
                    flipNoneToKey(user);
                } else noRegistratedClient(chatId);
                break;
            case "!!!!!Видалити акаунт!!!!!":
                if (dataBase.deleteClient(chatId) > 0) {
                    user.setState(REGISTRATION);
//                    TODO сделать запрос к бд на удаление всех страховок у юзера
                    bot.execute(bot.printText(chatId, "Акаунт видалено успішно. Щоб ввести повторно дані: /reg"));
                } else noRegistratedClient(chatId);
                break;
            default:
                break;
        }
    }

    @SneakyThrows
    void doEditAction(String dataText, Long chatId, User user) {
        switch (dataText) {
            case "Ім'я":
                break;
            case "Mail":
                break;
            case "Номер телефону":
                break;
            case "Видалити поліс":
                user.setState(EDIT);
                setActiveInsurances(chatId);
                if (!arrayOfActiveIns.isEmpty()) {
                    user.setState(POLICY_DELETE);
                    bot.execute(inlineKeyboard.buttonsForActiveInsurance(chatId, arrayOfActiveIns));
                    arrayOfActiveIns.clear();
                } else
                    bot.execute(bot.printText(chatId, "Немає активних страхувань для видалення"));
                break;
            case "Повернутися в головне меню":
                bot.onStepBack(user, client);
                break;
            default:
                break;
        }
    }

    @SneakyThrows
    public void doDeleteInsurance(String dataText, Long chatId) {
        for (String type : arrayOfTypes) {
            if (dataText.equals(type)) {
                dataBase.deleteInsurance(chatId, type);
                bot.execute(bot.printText(chatId, "Страховка " + type + " успішно видалена"));
                break;
            }
        }
    }

    @SneakyThrows
    public void setActiveInsurances(Long chatId) {
        for (String arrayOfType : arrayOfTypes) {
            if (dataBase.checkAvailability(chatId, arrayOfType))
                arrayOfActiveIns.add(arrayOfType);
        }
    }

    @SneakyThrows
    public void doInsuranceAction(User user, String dataText, Long chatId) {
        for (int i = 0; i < arrayOfTypes.size(); i++) {
            if (dataText.equals(arrayOfTypes.get(i))) {
                showInsuranceInfo(user, chatId, i + 1, dataText);
                user.setState(POLICY_ADD);
                break;
            }
        }
        if (dataText.equals("Повернутися в головне меню")) {
            bot.onStepBack(user, client);
        }
    }

    @SneakyThrows
    public void showActiveInsurances(InlineKeyboard inlineKeyboard, User user, Long chatId) {
        bot.execute(bot.printText(chatId, "Маєте активні страхування: "));
        String dataText;
        for (int i = 0; i < arrayOfTypes.size(); i++) {
            dataText = arrayOfTypes.get(i);
            if (dataBase.checkAvailability(chatId, dataText)) {
                bot.execute(bot.printText(chatId, dataBase.readInsurances(i + 1)));
            }
        }
        user.setState(POLICY_ADD);
        bot.execute(inlineKeyboard.insurancesIsRelevant(chatId, user.getState()));
    }

    @SneakyThrows
    private void showInsuranceInfo(User user, Long chatId, int count, String dataText) {
        typeName = dataText;
        bot.execute(bot.printText(chatId, dataBase.readInsurances(count)));
        bot.execute(inlineKeyboard.buttonsForAddingInsurance(chatId));
        user.setState(POLICY);
    }

    @SneakyThrows
    void doInsuranceTechAction(String dataText, User user) {
        Long chatId = user.getChatId();
        switch (dataText) {
            case "Додати поліс":
                onPolicy(user);
                if (!dataBase.checkAvailability(chatId, typeName.toLowerCase())) {
                    dataBase.addInsurance(chatId, typeName.toLowerCase());
                    bot.execute(bot.printText(chatId, "Тип страховки " + typeName + " успішно додано в список Ваших активних полісів"));
                    Thread.sleep(1000);
                    bot.execute(inlineKeyboard.buttonsForInsurance(user.getChatId(), arrayOfTypes));
                } else
                    bot.execute(inlineKeyboard.insurancesIsRelevant(chatId, user.getState()));
                break;
            case "Переглянути ще поліси":
                bot.execute(inlineKeyboard.buttonsForInsurance(chatId, arrayOfTypes));
                user.setState(POLICY);
                break;
            case "Повернутися в головне меню":
                bot.onStepBack(user, client);
                break;
        }
    }

    @SneakyThrows
    public void onPolicy(User user) {
        user.setState(POLICY_ADD);
    }

    void flipNoneToKey(User user) {
        if (user.getState() == NONE) user.setState(KEY);
    }

    @SneakyThrows
    public void noRegistratedClient(Long chatId) {
        bot.execute(bot.printText(chatId, "Ваш акаунт не зареєстрований. Щоб продовжити - натисніть /reg"));
    }

    @SneakyThrows
    public void doAdminAction(String dataText, User user) {
        Long chatId = user.getChatId();
        DataBase dataBase = new DataBase();
        switch (dataText) {
            case "Інформація про працівників":
                Set<String> keys = DataBase.redisDB.keys("worker:*");
                ArrayList<String> workers = new ArrayList<>();
                for (String key : keys)
                    workers.add(dataBase.showWorker(key));
                for (String worker : workers) bot.execute(bot.printText(chatId, worker));
                break;
            case "Інформація про клієнтів":
                ArrayList<String> clients = new ArrayList<>();
                for (int i = 0; i < dataBase.getClientsNumber(); i++)
                    clients.add(dataBase.showClient());
                for (int i = 0; i < clients.size(); i++)
                    bot.execute(bot.printText(chatId, clients.get(i)));
                break;
            case "Інформація про клієнта":
                user.setState(ADMIN_KEY);
                break;
        }
    }
}
