package com.example.mediBook;

import com.example.mediBook.models.User;
import com.example.mediBook.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        if (userRepository.findByTelephone("admin").isEmpty()) {
            User admin = new User();
            admin.setTelephone("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            // CORRECTION : setRole("ADMIN") → setRole(User.Role.ADMIN)
            // "ADMIN" est un String mais role est un enum User.Role
            // Java ne peut pas convertir automatiquement un String en enum
            admin.setRole(User.Role.ADMIN);
            userRepository.save(admin);
            System.out.println("Admin créé !");
        }
    }
}