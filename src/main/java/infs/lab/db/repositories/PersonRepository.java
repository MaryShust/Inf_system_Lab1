package infs.lab.db.repositories;

import infs.lab.db.entities.Person;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Person p WHERE p.id = :id")
    Optional<Person> findByIdWithLock(@Param("id") Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Person p WHERE p.name = :name AND p.height = :height")
    Optional<List<Person>> findByNameAndHeightWithLock(
            @Param("name") String name,
            @Param("height") int height
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Person p WHERE p.id != :id AND p.name = :name AND p.height = :height")
    Optional<List<Person>> findOtherPeopleWithNameAndHeightWithLock(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("height") int height
    );
}