package inf_system.Lab1.db.repositories;

import inf_system.Lab1.db.entities.Location;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> findByXAndYAndZ(double x, float y, double z);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT l FROM Location l WHERE l.x = :x AND l.y = :y AND l.z = :z")
    Location findByXAndYAndZWithLock(@Param("x") double x, @Param("y") float y, @Param("z") double z);
}
