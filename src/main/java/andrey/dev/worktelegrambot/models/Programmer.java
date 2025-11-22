package andrey.dev.worktelegrambot.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Table(name = "programmers")
public class Programmer {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String surname;

    private Long age;

    private String technologies;

    private String experience;

    private String aboutMyself;

    private String contacts;

    private String telegram;

    private String chatId;
}
