package inf_system.Lab1.db.repositories;

import inf_system.Lab1.db.entities.History;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {

    List<History> findByAuthor(String author);
}