package andrey.dev.worktelegrambot.services;

import andrey.dev.worktelegrambot.dto.VacancyMapper;
import andrey.dev.worktelegrambot.models.Vacancy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;

@Service
@Getter
@RequiredArgsConstructor
public class VacancyService {


    private HashSet<Long> waitingForMainInformationOfVacancy = new HashSet<>();

    private HashMap<Long, Vacancy> vacancyInCreating = new HashMap<>();

    private final VacancyMapper vacancyMapper;


    public void startCreatingVacancyCheck(Long chatId) {
        waitingForMainInformationOfVacancy.add(chatId);
    }

    public Vacancy mainVacancyInformationHandler(Long chatId, String message) {
        Vacancy vacancy;
        String[] info = message.split("\\s*;\\s*");
        if (info.length < 5) {
            throw new IllegalArgumentException();
        }
        try {
            vacancy = vacancyMapper.toVacancy(info, chatId);
        } catch (RuntimeException e) {
            throw new RuntimeException();
        }
        vacancyInCreating.put(chatId, vacancy);
        return vacancy;
    }
}
