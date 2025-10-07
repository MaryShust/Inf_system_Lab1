package inf_system.Lab1.services;

import inf_system.Lab1.db.entities.User;
import inf_system.Lab1.db.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Long findOrCreateUser(String name, String password) {
        // БЫСТРАЯ ПРОВЕРКА БЕЗ БЛОКИРОВКИ (для оптимизации)
        Optional<User> existingUser = userRepository.findByNameAndPassword(name, password);
        if (existingUser.isPresent()) {
            return existingUser.get().getId(); // Быстрый путь - пользователь уже существует
        }

        existingUser = userRepository.findByNameAndPasswordWithLock(name, password);

        // Double-check ПОД БЛОКИРОВКОЙ (на случай если между первой проверкой и блокировкой пользователь создался)
        if (existingUser.isPresent()) {
            return existingUser.get().getId();
        }

        User newUser = new User();
        newUser.setName(name);
        newUser.setPassword(password);
        User savedUser = userRepository.save(newUser);
        return savedUser.getId();
    }
}