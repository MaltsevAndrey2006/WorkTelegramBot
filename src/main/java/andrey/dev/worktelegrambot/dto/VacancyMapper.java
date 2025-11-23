package andrey.dev.worktelegrambot.dto;

import andrey.dev.worktelegrambot.models.Vacancy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface VacancyMapper {
    @Mapping(target = "name", expression = "java(info[0])")
    @Mapping(target = "aboutCompany", expression = "java(info[1])")
    @Mapping(target = "city", expression = "java(info[2])")
    @Mapping(target = "salary", expression = "java(toDecimal(info[3]))")
    @Mapping(target = "contacts", expression = "java(info[4])")
    @Mapping(target = "chatId", expression = "java(String.valueOf(chatId))")
    Vacancy toVacancy(String[] info, Long chatId);

    default BigDecimal toDecimal(String strSalary) {
        try {
            BigDecimal salary = BigDecimal.valueOf(Long.parseLong(strSalary));
            if (0 > salary.doubleValue()) {
                throw new IllegalArgumentException();
            }
            return salary;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException();
        }
    }
}
