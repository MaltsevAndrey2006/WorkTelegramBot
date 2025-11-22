package andrey.dev.worktelegrambot.repositoreies;

import andrey.dev.worktelegrambot.models.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
}
