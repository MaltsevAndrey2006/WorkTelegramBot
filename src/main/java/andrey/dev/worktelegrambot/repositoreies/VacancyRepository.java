package andrey.dev.worktelegrambot.repositoreies;

import andrey.dev.worktelegrambot.models.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Long> {

}
