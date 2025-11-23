package andrey.dev.worktelegrambot.services;

import andrey.dev.worktelegrambot.models.Programmer;
import andrey.dev.worktelegrambot.models.Vacancy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@RequiredArgsConstructor
public class MessageHandlerService {

    private final MessageSenderService messageSenderService;

    public void personalDataHandler(Long chatId, String messageText, ProgrammerRegistrationService programmerRegistrationService, TelegramClient telegramClient) {
        Programmer programmer;
        try {
            programmer = programmerRegistrationService.personalDataHandler(chatId, messageText);
        } catch (IllegalArgumentException e) {
            messageSenderService.sendMessage(chatId, "введите все ваши данные через пробел пример : Иванов Иван Иванович 18 ", telegramClient);
            return;
        } catch (RuntimeException r) {
            messageSenderService.sendMessage(chatId, "введите число где должен стоять возраст , также ваш возраст должен быть >=18  , пример:Иванов Иван Иванович 18", telegramClient);
            return;
        }
        messageSenderService.sendMessage(chatId, String.format("ваши данные :\nфамилия: %s\n имя: %s\n отчество: %s\n возраст: %s \nпозже вы сможете поменять эти данные"
                , programmer.getLastName()
                , programmer.getFirstName()
                , programmer.getSurname()
                , programmer.getAge()), telegramClient);
    }

    public void techStackHandler(CallbackQuery callbackQuery, ProgrammerRegistrationService programmerRegistrationService, TelegramClient telegramClient) {
        Long chatId = callbackQuery.getFrom().getId();
        try {
            Programmer programmer = programmerRegistrationService.techStackHandler(callbackQuery);
            if (programmer != null) {
                messageSenderService.sendMessage(chatId, String.format("ваши навыки: %s \nпозже вы сможете их поменять", programmer.getTechnologies()), telegramClient);
                messageSenderService.restInformationMessage(chatId, telegramClient);
            }
        } catch (RuntimeException e) {
            messageSenderService.sendMessage(chatId, "Выберите хотя бы один навык", telegramClient);
        }
    }

    public void restInformationHandler(Long chatId, String messageText, TelegramClient telegramClient, ProgrammerRegistrationService programmerRegistrationService) {
        Programmer programmer;
        try {
            programmer = programmerRegistrationService.restInformationHandler(chatId, messageText);
        } catch (IllegalArgumentException e) {
            messageSenderService.sendMessage(chatId, "Введите как ваши данные как в примере.Пример: 3; Люблю программировать , спать , и деньги ; мои контакты: инста... , тик ток... ", telegramClient);
            return;
        } catch (RuntimeException r) {
            messageSenderService.sendMessage(chatId, "введите опыт работы как в примере. Пример: 3; Люблю программировать , спать , и деньги ; мои контакты: инста... , тик ток...  ", telegramClient);
            return;
        }
        messageSenderService.sendMessage(chatId, String.format("ваш итоговый профиль: \nфамилия: %s \nимя: %s \nотчество: %s \nвозраст: %s " +
                        " \nзнания: %s \nопыт: %s " +
                        (Integer.parseInt(programmer.getExperience()) > 4 || Integer.parseInt(programmer.getExperience()) == 0 ? "лет" :
                                (Integer.parseInt(programmer.getExperience()) == 1 ? "год" : "года"))
                        + "\nо вас: %s \nконтакты: %s \nпозже вы сможете поменять эти данные"
                , programmer.getLastName(), programmer.getFirstName(), programmer.getSurname(), programmer.getAge(), programmer.getTechnologies()
                , programmer.getExperience(), programmer.getAboutMyself(), programmer.getContacts()), telegramClient);
    }

    public void mainVacancyInformationHandler(Long chatId, String text, TelegramClient telegramClient, VacancyService vacancyService) {
        Vacancy vacancy;
        try {
            vacancy = vacancyService.mainVacancyInformationHandler(chatId, text);
        } catch (IllegalArgumentException i) {
            messageSenderService.sendMessage(chatId, "введите данные как в примере", telegramClient);
            return;
        } catch (RuntimeException r) {
            messageSenderService.sendMessage(chatId, "введите зарплату положительной и числом как в примере  ", telegramClient);
            return;
        }
        messageSenderService.sendMessage(chatId, String.format("дынные которые вы ввели : \nназвание компании: %s \nописание: %s \nгород: %s \nзарплата: \nконтакты %s "
                , vacancy.getName(), vacancy.getAboutCompany(), vacancy.getCity(), vacancy.getSalary(), vacancy.getContacts()), telegramClient);
    }

}
