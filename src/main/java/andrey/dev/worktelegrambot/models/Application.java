package andrey.dev.worktelegrambot.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "application")
public class Application {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long programmerId;

    private Long vacancyId;

    @CreationTimestamp
    private LocalDate applicationDate;

    private String status;
}
