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
//        Optional<User> existingUser = userRepository.findByNameAndPassword(name, password);
        Optional<User> existingUser = userRepository.findByNameAndPasswordWithLock(name, password);
        Long userId;
        if (existingUser.isPresent()) {
            userId = existingUser.get().getId();
        } else {
            User newUser = new User();
            newUser.setName(name);
            newUser.setPassword(password);
            User savedUser = userRepository.save(newUser);
            userId = savedUser.getId();
        }
        return userId;
    }
}
