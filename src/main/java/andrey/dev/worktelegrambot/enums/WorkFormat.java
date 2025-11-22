package andrey.dev.worktelegrambot.enums;

import lombok.Getter;

@Getter
public enum WorkFormat {
    OFFICE("office", "в офисе"), REMOTE("remote", "удаленный"),
    HYBRID("hybrid", "гибрид");

    private final String name;
    private final String nameInRussian;

    WorkFormat(String name, String nameInRussian) {
        this.name = name;
        this.nameInRussian = nameInRussian;
    }
}
