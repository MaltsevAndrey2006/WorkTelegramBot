package andrey.dev.worktelegrambot.enums;

import lombok.Getter;

@Getter
public enum AgeExperience {
    NO_EXPERIENCE("нет опыта ", "no_experience"), MIN_EXPERIENCE("от 1 до 3 лет опыта", "min_experience"
    ), MIDDLE_EXPERIENCE("от 3 до 6 лет опыта ", "middle_experience"), MAX_EXPERIENCE("6 лет опыта и выше", "max_experience");

    private final String inAges;
    private final String name;

    AgeExperience(String inAges, String name) {
        this.inAges = inAges;
        this.name = name;
    }
}
