package inf_system.Lab1.db.repositories;

import inf_system.Lab1.db.entities.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByNameAndPassword(String name, String password);

    // нужно для предотвращения одновременного создания дубликатов
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM User u WHERE u.name = :name AND u.password = :password")
    Optional<User> findByNameAndPasswordWithLock(@Param("name") String name, @Param("password") String password);
}