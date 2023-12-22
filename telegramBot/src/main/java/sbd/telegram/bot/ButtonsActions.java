package sbd.telegram.bot;

import lombok.SneakyThrows;
import sbd.telegram.controllers.User;
import sbd.telegram.database.Client;
import sbd.telegram.database.DataBase;

import static sbd.telegram.controllers.State.*;

public class ButtonsActions {

    private Bot bot;
    private Client client;
    private DataBase dataBase;
    static String typeName;

    public ButtonsActions() {
        this.bot = new Bot();
        this.dataBase = new DataBase();
        this.client = new Client();
    }

    @SneakyThrows
    void doKeyAction(InlineKeyboard inlineKeyboard, String dataText, Long chatId, User user) {
        switch (dataText) {
            case "Змінити данні акаунту":
//                TODO сделать запрос к бд на замену конкретных данных
                if (client.isValidClient(chatId)) {
                    bot.execute(inlineKeyboard.buttonsForEditUser(chatId));
                    flipNoneToKey(user);
                } else noRegistratedClient(chatId);
                break;
            case "Оформити страхування":
                if (client.isValidClient(chatId)) {
                    bot.execute(inlineKeyboard.buttonsForInsurance(chatId));
                    flipNoneToKey(user);
                } else noRegistratedClient(chatId);
                break;
            case "Переглянути активні страхування":
                if (client.isValidClient(chatId)) {
//                    TODO сделать запрос на чтение данных в бд через внешний ключ insuranceId
                    bot.execute(bot.printText(chatId, "Будет список страховок"));
                    flipNoneToKey(user);
                } else noRegistratedClient(chatId);
                break;
            case "!!!!!Видалити акаунт!!!!!":
                if (dataBase.deleteClient(chatId) > 0) {
                    user.setState(REGISTRATION);
                    bot.execute(bot.printText(chatId, "Акаунт видалено успішно. Щоб ввести повторно дані: /reg"));
                } else noRegistratedClient(chatId);
                break;
            default:
                break;
        }
    }

    @SneakyThrows
    void doForEditUserAction(InlineKeyboard inlineKeyboard, String dataText, Long chatId, User user) {
        switch (dataText) {
            case "Ім'я":
                break;
            case "Mail":
                break;
            case "Номер телефону":
                break;
            case "Видалити поліс":
                user.setState(POLICY_DELETE);
//                TODO getCountAndTypeInsurances должна возвращать количество страховок и тип страховки
//                dataBase.deleteInsurance(chatId, typeName);
//                bot.execute(inlineKeyboard.);
                break;
            case "Повернутися в головне меню":
                bot.onStepBack(user, client);
                break;
            default:
                break;
        }
    }

    @SneakyThrows
    public void doInsuranceAction(InlineKeyboard inlineKeyboard, User user, String dataText, Long chatId) {
        int count;
        switch (dataText) {
            case "Car":
                count = 1;
                insuranceInfo(inlineKeyboard, chatId, user, count, dataText);
                break;
            case "Medical":
                count = 2;
                insuranceInfo(inlineKeyboard, chatId, user, count, dataText);
                break;
            case "Life":
                count = 3;
                insuranceInfo(inlineKeyboard, chatId, user, count, dataText);
                break;
            case "RealEstate":
                count = 4;
                insuranceInfo(inlineKeyboard, chatId, user, count, dataText);
                break;
            case "Business":
                count = 5;
                insuranceInfo(inlineKeyboard, chatId, user, count, dataText);
                break;
            case "Повернутися в головне меню":
                bot.onStepBack(user, client);
                break;
            default:
                break;
        }
    }

    @SneakyThrows
    private void insuranceInfo(InlineKeyboard inlineKeyboard, Long chatId, User user, int count, String dataText) {
        typeName = dataText;
        bot.execute(bot.printText(chatId, dataBase.readInsurances(count)));
        bot.execute(inlineKeyboard.buttonsForAddingInsurance(chatId));
        user.setState(POLICY);
    }

    @SneakyThrows
    void doInsuranceTechAction(String dataText, User user) {
        InlineKeyboard inlineKeyboard = new InlineKeyboard();
        Long chatId = user.getChatId();
        switch (dataText) {
            case "Додати поліс":
//                    TODO СДЕЛАТЬ ПРОВЕРКУ СПИСКА ПОЛИСОВ - ЕСЛИ ТАКОЙ УЖЕ ЕСТЬ - СКИП
                onPolicy(user);
                if (!dataBase.checkAvailability(chatId, typeName.toLowerCase())) {
                    dataBase.addInsurance(chatId, typeName.toLowerCase());
                    bot.execute(bot.printText(chatId, "Тип страховки " + typeName + " успішно добавлено список Ваших активних полісів"));
                    Thread.sleep(1000);
                    bot.execute(inlineKeyboard.buttonsForInsurance(user.getChatId()));
                } else
                    bot.execute(inlineKeyboard.insurancesIsRelevant(chatId));
                break;
            case "Переглянути ще поліси":
                bot.execute(inlineKeyboard.buttonsForInsurance(chatId));
                break;
            case "Повернутися в головне меню":
                if (user.getState() == POLICY)
                    bot.onStepBack(user, client);
                break;
        }
    }

    @SneakyThrows
    public void onPolicy(User user) {
        user.setState(POLICY);
    }

    void flipNoneToKey(User user) {
        if (user.getState() == NONE) user.setState(KEY);
    }

    @SneakyThrows
    public void noRegistratedClient(Long chatId) {
        bot.execute(bot.printText(chatId, "Ваш акаунт не зареэстрований. Щоб продовжити - натисніть /reg"));
    }
}
