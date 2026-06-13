package com.example.mediBook.service;

import com.example.mediBook.models.User;
import com.example.mediBook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Inscription
    public User inscrire(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Connexion par téléphone
    public Optional<User> connecterParTelephone(String telephone, String password) {
        Optional<User> user = userRepository.findByTelephone(telephone);
        if (user.isPresent() &&
                passwordEncoder.matches(password, user.get().getPassword())) {
            return user;
        }
        return Optional.empty();
    }

    // Vérifier si téléphone existe
    public boolean telephoneExiste(String telephone) {
        return userRepository.existsByTelephone(telephone);
    }
}
