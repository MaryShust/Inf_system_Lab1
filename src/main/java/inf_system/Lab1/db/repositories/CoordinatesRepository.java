package inf_system.Lab1.db.repositories;

import inf_system.Lab1.db.entities.Coordinates;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CoordinatesRepository extends JpaRepository<Coordinates, Long> {

    Optional<Coordinates> findByXAndY(int x, int y);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Coordinates c WHERE c.x = :x AND c.y = :y")
    Coordinates findByXAndYWithLock(@Param("x") int x, @Param("y") int y);
}
