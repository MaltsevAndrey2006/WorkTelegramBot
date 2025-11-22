package andrey.dev.worktelegrambot.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vacancies")
public class Vacancy {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String technologies;

    private String experience;

    private String aboutCompany;

    private String contacts;

    private String level;

    private String telegram;

    private BigDecimal salary;

    private String schedule;

    private String workFormat;

    private LocalDate publicationDate;

    private String city;

    private String chatId;
}
