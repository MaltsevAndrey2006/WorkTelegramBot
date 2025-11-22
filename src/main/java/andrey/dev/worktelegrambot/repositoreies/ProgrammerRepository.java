package andrey.dev.worktelegrambot.repositoreies;

import andrey.dev.worktelegrambot.models.Programmer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgrammerRepository extends JpaRepository<Programmer, Long> {

}
