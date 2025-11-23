package andrey.dev.worktelegrambot.services;

import andrey.dev.worktelegrambot.dto.ProgrammerMapper;
import andrey.dev.worktelegrambot.enums.TechStack;
import andrey.dev.worktelegrambot.models.Programmer;
import andrey.dev.worktelegrambot.repositoreies.ProgrammerRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.HashMap;
import java.util.HashSet;

@RequiredArgsConstructor
@Service
@Getter
public class ProgrammerRegistrationService {

    private HashSet<Long> waitingForName = new HashSet<>();

    private HashSet<Long> waitingForTechnologies = new HashSet<>();

    private HashSet<Long> waitingForRestInformation = new HashSet<>();

    private final ProgrammerMapper programmerMapper;

    private HashMap<Long, Programmer> mapOfProgrammersInRegistration = new HashMap<>();

    private HashMap<Long, HashSet<TechStack>> techStacksOfProgrammers = new HashMap<>();

    private final ProgrammerRepository programmerRepository;

    public void beginOfRegistration(Long chatId) {
        waitingForName.add(chatId);
    }

    public Programmer personalDataHandler(Long chatId, String messageText) {
        Programmer programmer;
        String[] personalInfo = messageText.split(" ");
        if (personalInfo.length < 4) {
            throw new IllegalArgumentException();
        }
        try {
            programmer = programmerMapper.fromPersonalInfo(personalInfo, chatId);
        } catch (RuntimeException e) {
            throw new RuntimeException();
        }
        mapOfProgrammersInRegistration.put(chatId, programmer);
        waitingForName.remove(chatId);
        waitingForTechnologies.add(chatId);
        return programmer;
    }

    public Programmer techStackHandler(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getFrom().getId();

        if (!callbackQuery.getData().equals("tech_submit")) {
            HashSet<TechStack> techStacks;
            if (techStacksOfProgrammers.containsKey(chatId)) {
                techStacks = techStacksOfProgrammers.get(chatId);
            } else {
                techStacks = new HashSet<>();
            }
            techStacks.add(TechStack.valueOf(callbackQuery.getData()));
            techStacksOfProgrammers.put(chatId, techStacks);
        } else {
            if (techStacksOfProgrammers.containsKey(chatId)) {
                String username = callbackQuery.getFrom().getUserName();
                Programmer programmer = mapOfProgrammersInRegistration.get(chatId);
                programmer.setTechnologies(fromSetsOfEnumToString(techStacksOfProgrammers.get(chatId)));
                programmer.setTelegram(username);
                waitingForTechnologies.remove(chatId);
                waitingForRestInformation.add(chatId);
                return programmer;
            } else {
                throw new RuntimeException();
            }

        }
        return null;
    }

    private String fromSetsOfEnumToString(HashSet<TechStack> techStacks) {
        StringBuilder str = new StringBuilder();
        for (TechStack ts : techStacks) {
            str.append(ts.getName()).append(" ,");
        }
        return str.toString();
    }

    public Programmer restInformationHandler(Long chatId, String messageText) {
        String[] array = messageText.trim().split("\\s*;\\s*");
        if (array.length < 3) {
            throw new IllegalArgumentException();
        }
        Programmer programmer = mapOfProgrammersInRegistration.get(chatId);
        try {
            Long exp = Long.valueOf(array[0]);
            if (exp < 0) {
                throw new IllegalArgumentException();
            }
            programmer.setExperience(array[0]);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException();
        }
        programmer.setAboutMyself(array[1]);
        programmer.setContacts(array[2]);
        mapOfProgrammersInRegistration.put(chatId, programmer);
        waitingForRestInformation.remove(chatId);
        programmerRepository.save(programmer);
        return programmer;
    }

    public boolean isExist(Long chatId) {
        return programmerRepository.findByChatId(String.valueOf(chatId)) != null;
    }

}
