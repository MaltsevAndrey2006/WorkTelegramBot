package andrey.dev.worktelegrambot.enums;

import lombok.Getter;

@Getter
public enum ExperienceLevel {
    TRAINER("trainer"), JUNIOR("junior"), MIDDLE("middle"), SENIOR("senior"), LEAD("lead");

    private final String name;

    @Override
    public String toString() {
        return this.name;
    }

    ExperienceLevel(String name) {
        this.name = name;
    }
}
