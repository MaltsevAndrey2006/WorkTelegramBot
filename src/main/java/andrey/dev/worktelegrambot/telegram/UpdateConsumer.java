package andrey.dev.worktelegrambot.telegram;

import andrey.dev.worktelegrambot.services.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
public class UpdateConsumer implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;

    private final ProgrammerRegistrationService programmerRegistrationService;

    private final MessageHandlerService messageHandler;

    private final InlineButtonsSenderService inlineButtonsHandler;

    private final MessageSenderService messageSenderService;

    private final VacancyService vacancyService;


    public UpdateConsumer(@Value("${telegram.bot.token}") String token, ProgrammerRegistrationService programmerRegistrationService, MessageHandlerService messageHandler, InlineButtonsSenderService inlineButtonsHandler, MessageSenderService messageSenderService, VacancyService vacancyService) {
        this.telegramClient = new OkHttpTelegramClient(token);
        this.programmerRegistrationService = programmerRegistrationService;
        this.messageHandler = messageHandler;
        this.inlineButtonsHandler = inlineButtonsHandler;
        this.messageSenderService = messageSenderService;
        this.vacancyService = vacancyService;
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
            } else if (vacancyService.getWaitingForMainInformationOfVacancy().contains(chatId)) {
                mainInfoVacancyHandler(chatId, messageText);
                messageText = null;
            }

            switch (messageText) {
                case "/start", "Выход" -> {
                    sendStartMenu(chatId);
                    break;
                }
                case "программист" -> {
                    if (!programmerRegistrationService.isExist(chatId)) {
                        startProgrammerBranch(chatId);
                    } else {
                        System.out.println("ты есть ");
                    }
                    break;
                }
                case "HR", "назад к hr" -> hrMainMenu(chatId);
                case "вакансии" -> {
                    vacancyMainMenu(chatId);
                }
                case "создать вакансию" -> startCreatingVacancyBranch(chatId);
                case null -> {
                }
                default ->
                        messageSenderService.sendMessage(chatId, "я вас не понимаю. Вы можете  ввести команду /start ", telegramClient);


            }
        } else if (update.hasCallbackQuery()) {
            if (programmerRegistrationService.getWaitingForTechnologies().contains(update.getCallbackQuery().getFrom().getId())) {
                techStackHandler(update.getCallbackQuery());
            }
        }
    }

    private void mainInfoVacancyHandler(Long chatId, String info) {
        messageHandler.mainVacancyInformationHandler(chatId, info, telegramClient, vacancyService);
    }

    private void startCreatingVacancyBranch(Long chatId) {
        vacancyService.startCreatingVacancyCheck(chatId);
        messageSenderService.startCreatingVacancyBranch(chatId, telegramClient);
    }

    private void vacancyMainMenu(Long chatId) {
        messageSenderService.vacancyMainMenu(chatId, telegramClient);
    }

    private void hrMainMenu(Long chatId) {
        messageSenderService.hrMainMenu(chatId, telegramClient);
    }


    private void restInformationHandler(Long chatId, String messageText) {
        messageHandler.restInformationHandler(chatId, messageText, telegramClient, programmerRegistrationService);

    }

    private void techStackHandler(CallbackQuery callbackQuery) {
        messageHandler.techStackHandler(callbackQuery, programmerRegistrationService, telegramClient);
    }

    private void personalDataHandler(Long chatId, String messageText) {
        messageHandler.personalDataHandler(chatId, messageText, programmerRegistrationService, telegramClient);
        sendTechStackSelection(chatId);
    }

    private void startProgrammerBranch(Long chatId) {
        messageSenderService.startProgrammerBranch(chatId, telegramClient);
        programmerRegistrationService.beginOfRegistration(chatId);
    }

    private void sendTechStackSelection(Long chatId) {
        inlineButtonsHandler.sendTechStackSelection(chatId, telegramClient);
    }

    private void sendStartMenu(Long chatId) {
        messageSenderService.sendStartMenu(chatId, telegramClient);
    }
}
