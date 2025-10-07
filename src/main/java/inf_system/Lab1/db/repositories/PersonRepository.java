package inf_system.Lab1.db.repositories;

import inf_system.Lab1.db.entities.Person;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Person p WHERE p.id = :id")
    Optional<Person> findByIdWithLock(@Param("id") Long id);
}