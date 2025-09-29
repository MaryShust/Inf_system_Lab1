package inf_system.Lab1.db.repositories;

import inf_system.Lab1.db.entities.Person;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query("SELECT COUNT(p) FROM Person p WHERE p.location.id = :locationId")
    Long countByLocationId(@Param("locationId") Long locationId);

    @Query("SELECT COUNT(p) FROM Person p WHERE p.coordinates.id = :coordinatesId")
    Long countByCoordinatesId(@Param("coordinatesId") Long coordinatesId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Person p WHERE p.id = :id")
    Optional<Person> findByIdWithLock(@Param("id") Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT COUNT(p) FROM Person p WHERE p.location.id = :locationId")
    Long countByLocationIdWithLock(@Param("locationId") Long locationId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT COUNT(p) FROM Person p WHERE p.coordinates.id = :coordinatesId")
    Long countByCoordinatesIdWithLock(@Param("coordinatesId") Long coordinatesId);
}