package andrey.dev.worktelegrambot.telegram;

import andrey.dev.worktelegrambot.enums.TechStack;
import andrey.dev.worktelegrambot.models.Programmer;
import andrey.dev.worktelegrambot.services.ProgrammerRegistrationService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.List;

@Component
public class UpdateConsumer implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;
    private final ProgrammerRegistrationService programmerRegistrationService;

    public UpdateConsumer(@Value("${telegram.bot.token}") String token, ProgrammerRegistrationService programmerRegistrationService) {
        this.telegramClient = new OkHttpTelegramClient(token);
        this.programmerRegistrationService = programmerRegistrationService;
    }

    @Override
    public void consume(Update update) {

        if (update.hasMessage()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            if (programmerRegistrationService.getWaitingForName().contains(chatId)) {
                personalDataHandler(chatId, messageText);
                messageText = null;
            } else if (programmerRegistrationService.getWaitingForRestInformation().contains(chatId)) {
                restInformationHandler(chatId, messageText);
                messageText = null;
            }

            switch (messageText) {
                case "/start" -> sendStartMenu(chatId);
                case "программист" -> {
                    if (!programmerRegistrationService.isExist(chatId)) {
                        startProgrammerBranch(chatId);
                    } else {
                        System.out.println("ты есть ублюдок ");
                    }
                }
                case null -> {
                }
                default -> sendMessage(chatId, "я вас не понимаю. Вы можете  ввести команду /start ");
            }
        } else if (update.hasCallbackQuery()) {
            if (programmerRegistrationService.getWaitingForTechnologies().contains(update.getCallbackQuery().getFrom().getId())) {
                techStackHandler(update.getCallbackQuery());
            }
        }
    }

    private void restInformationHandler(Long chatId, String messageText) {
        Programmer programmer;
        try {
            programmer = programmerRegistrationService.restInformationHandler(chatId, messageText);
        } catch (IllegalArgumentException e) {
            sendMessage(chatId, "Введите как ваши данные как в примере.Пример: 3; Люблю программировать , спать , и деньги ; мои контакты: инста... , тик ток... ");
            return;
        } catch (RuntimeException r) {
            sendMessage(chatId, "введите опыт работы как в примере. Пример: 3; Люблю программировать , спать , и деньги ; мои контакты: инста... , тик ток...  ");
            return;
        }
        sendMessage(chatId, String.format("ваш итоговый профиль: \nфамилия: %s \nимя: %s \nотчество: %s \nвозраст: %s лет" +
                        " \nзнания: %s \nопыт: %s "+
                        (Integer.parseInt(programmer.getExperience())>4||Integer.parseInt(programmer.getExperience())==0?"лет":
                                (Integer.parseInt(programmer.getExperience())==1?"год" :"года"))
                        +"\nо вас: %s \nконтакты: %s \nпозже вы сможете поменять эти данные"
                , programmer.getLastName(), programmer.getFirstName(), programmer.getSurname(), programmer.getAge(), programmer.getTechnologies()
                , programmer.getExperience(), programmer.getAboutMyself(), programmer.getContacts()));

    }

    private void restInformationMessage(Long chatId) {
        sendMessage(chatId, "введите ваш опыт работы , информацию о вас(цели и т.д) и ваши контакты по мимо телеграмма через точкус запятой  ");
    }

    private void techStackHandler(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getFrom().getId();
        try {
            Programmer programmer = programmerRegistrationService.techStackHandler(callbackQuery);
            if (programmer != null) {
                sendMessage(chatId, String.format("ваши навыки: %s \nпозже вы сможете их поменять", programmer.getTechnologies()));
                restInformationMessage(chatId);
            }
        } catch (RuntimeException e) {
            sendMessage(chatId, "Выберите хотя бы один навык");
        }

    }


    private void personalDataHandler(Long chatId, String messageText) {
        Programmer programmer;
        try {
            programmer = programmerRegistrationService.personalDataHandler(chatId, messageText);
        } catch (IllegalArgumentException e) {
            sendMessage(chatId, "введите все ваши данные через пробел пример : Иванов Иван Иванович 18 ");
            return;
        } catch (RuntimeException r) {
            sendMessage(chatId, "введите число где должен стоять возраст , также ваш возраст должен быть >=18  , пример:Иванов Иван Иванович 18");
            return;
        }
        sendMessage(chatId, String.format("ваши данные :\nфамилия: %s\n имя: %s\n отчество: %s\n возраст: %s \nпозже вы сможете поменять эти данные"
                , programmer.getLastName()
                , programmer.getFirstName()
                , programmer.getSurname()
                , programmer.getAge()));
        sendTechStackSelection(chatId);
    }

    @SneakyThrows
    private void startProgrammerBranch(Long chatId) {
        telegramClient.execute(SendMessage
                .builder().text("Введите ваше фамилию , имя ,  отчество и возраст через пробел")
                .replyMarkup(ReplyKeyboardRemove.builder()
                        .removeKeyboard(true)
                        .build())
                .chatId(chatId).build());
        programmerRegistrationService.beginOfRegistration(chatId);
    }

    @SneakyThrows
    private void sendTechStackSelection(Long chatId) {
        String messageText = "💻 Выберите технологии которые вы знаете (можно несколько):";

        List<InlineKeyboardRow> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> currentRow = new ArrayList<>();

        int buttonsInRow = 2;

        for (int i = 0; i < TechStack.values().length; i++) {
            TechStack tech = TechStack.values()[i];

            InlineKeyboardButton button = InlineKeyboardButton.builder()
                    .text(tech.getName())
                    .callbackData(tech.name())
                    .build();

            currentRow.add(button);

            if (currentRow.size() == buttonsInRow || i == TechStack.values().length - 1) {
                keyboard.add(new InlineKeyboardRow(currentRow));
                currentRow = new ArrayList<>();
            }
        }

        keyboard.add(new InlineKeyboardRow(
                InlineKeyboardButton.builder()
                        .text("📤 Отправить выбор")
                        .callbackData("tech_submit")
                        .build()
        ));

        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder()
                .keyboard(keyboard)
                .build();

        SendMessage message = SendMessage.builder()
                .chatId(chatId.toString())
                .text(messageText)
                .replyMarkup(markup)
                .build();

        telegramClient.execute(message);
    }

    @SneakyThrows
    private void sendStartMenu(Long chatId) {
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
    private void sendMessage(Long chatId, String mainMessage) {
        SendMessage message = SendMessage.builder().text(mainMessage).chatId(chatId).build();
        telegramClient.execute(message);
    }

}
