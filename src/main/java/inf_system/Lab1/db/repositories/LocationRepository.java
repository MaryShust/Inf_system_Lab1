package inf_system.Lab1.db.repositories;

import inf_system.Lab1.db.entities.Location;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LocationRepository extends JpaRepository<Location, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT l FROM Location l WHERE l.x = :x AND l.y = :y AND l.z = :z")
    Location findByXAndYAndZWithLock(@Param("x") double x, @Param("y") float y, @Param("z") double z);

    @Modifying
    @Query("DELETE FROM Location l WHERE l.id = :id AND " +
            "(SELECT COUNT(p) FROM Person p WHERE p.location.id = :id) = 0")
    void deleteIfUnused(@Param("id") Long id);
}