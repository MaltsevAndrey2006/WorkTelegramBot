package andrey.dev.worktelegrambot.services;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

@Component
public class MessageSenderService {
    @SneakyThrows
    public void sendMessage(Long chatId, String mainMessage, TelegramClient telegramClient) {
        SendMessage message = SendMessage.builder().text(mainMessage).chatId(chatId).build();
        telegramClient.execute(message);
    }

    @SneakyThrows
    public void sendStartMenu(Long chatId, TelegramClient telegramClient) {
        telegramClient.execute(SendMessage
                .builder()
                .text("Выберите , вы ищите работу(программист) или вы ищите программиста(HR)")
                .chatId(chatId)
                .replyMarkup
                        (ReplyKeyboardMarkup.builder()
                                .keyboard(List.of(new KeyboardRow("программист", "HR")))
                                .resizeKeyboard(true)
                                .oneTimeKeyboard(true)
                                .build())
                .build());
    }

    @SneakyThrows
    public void startProgrammerBranch(Long chatId, TelegramClient telegramClient) {
        telegramClient.execute(SendMessage
                .builder().text("Введите ваше фамилию , имя ,  отчество и возраст через пробел")
                .replyMarkup(ReplyKeyboardRemove.builder()
                        .removeKeyboard(true)
                        .build())
                .chatId(chatId).build());
    }

    void restInformationMessage(Long chatId, TelegramClient telegramClient) {
        sendMessage(chatId, "введите ваш опыт работы , информацию о вас(цели и т.д) и ваши контакты по мимо телеграмма через точкус запятой ", telegramClient);
    }


    public void hrMainMenu(Long chatId, TelegramClient telegramClient) {
        buttonTemplate(chatId, List.of("Выход", "вакансии", "найти программиста"), telegramClient);
    }

    public void vacancyMainMenu(Long chatId, TelegramClient telegramClient) {
        buttonTemplate(chatId, List.of("создать вакансию", "посмотреть мои вакансии"
                , "редактировать вакансию", "удалить вакансию", "назад к hr"), telegramClient);
    }

    @SneakyThrows
    public void startCreatingVacancyBranch(Long chatId, TelegramClient telegramClient) {
        telegramClient.execute(SendMessage
                .builder().text("введите информацию про вакансию, а именно : должность в вакансии, " +
                        " о вашей компании, город, в котором находится ваша компания/работа ,зарплату и контакты через ; .\n" +
                        "Пример: ТруКомпания ; наша компания занимается программированием у нас вы будете заниматься тем-то тем-то " +
                        "; Москва ; 2000 ;наш сайт: https://... ")
                .replyMarkup(ReplyKeyboardRemove.builder()
                        .removeKeyboard(true)
                        .build())
                .chatId(chatId).build());
    }

    @SneakyThrows
    private void buttonTemplate(Long chatId, List<String> buttons, TelegramClient telegramClient) {
        KeyboardRow row = new KeyboardRow();
        for (String btn : buttons) {
            row.add(new KeyboardButton(btn));
        }
        telegramClient.execute(SendMessage
                .builder()
                .chatId(chatId)
                .text("выберите действие")
                .replyMarkup
                        (ReplyKeyboardMarkup.builder()
                                .keyboard(List.of(row))
                                .resizeKeyboard(true)
                                .oneTimeKeyboard(true)
                                .build())
                .build());
    }


}
