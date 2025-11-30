package infs.lab.db.repositories;

import infs.lab.db.entities.History;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {

    List<History> findByAuthor(String author);
}