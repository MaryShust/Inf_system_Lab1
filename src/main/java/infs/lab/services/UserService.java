package infs.lab.services;

import infs.lab.aop.CacheLogging;
import infs.lab.db.entities.User;
import infs.lab.db.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @CacheLogging
    @Transactional
    public Long findOrCreateUser(String name, String password) {

        Optional<User> existingUser = userRepository.findByNameAndPassword(name, password);
        if (existingUser.isPresent()) {
            return existingUser.get().getId(); // Быстрый путь - пользователь уже существует
        }

        User newUser = new User();
        newUser.setName(name);
        newUser.setPassword(password);
        User savedUser = userRepository.save(newUser);
        return savedUser.getId();
    }
}