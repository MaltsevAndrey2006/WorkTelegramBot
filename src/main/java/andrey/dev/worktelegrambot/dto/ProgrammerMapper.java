package andrey.dev.worktelegrambot.dto;

import andrey.dev.worktelegrambot.models.Programmer;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProgrammerMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "firstName", expression = "java(personalInfo[1])")
    @Mapping(target = "lastName", expression = "java(personalInfo[0])")
    @Mapping(target = "surname", expression = "java(personalInfo[2])")
    @Mapping(target = "age", expression = "java(checkAge(personalInfo[3]))")
    @Mapping(target = "chatId", expression = "java(String.valueOf(chatId))")
    Programmer fromPersonalInfo(String[] personalInfo, Long chatId);

    default Long checkAge(String strAge) {
        try {
            Long age = Long.valueOf(strAge);
            if (age < 18) {
                throw new IllegalArgumentException();
            }
            return age;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException();
        }

    }
}
