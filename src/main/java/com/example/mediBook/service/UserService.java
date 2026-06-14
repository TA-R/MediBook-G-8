package com.example.mediBook.service;

import com.example.mediBook.models.User;
import com.example.mediBook.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService  implements UserDetailsService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User inscrire(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public boolean telephoneExiste(String telephone) {
        return userRepository.existsByTelephone(telephone);
    }

    public Optional<User> findByTelephone(String telephone) {
        return userRepository.findByTelephone(telephone);
    }

    // CORRECTION : cette méthode est OBLIGATOIRE quand on implements UserDetailsService.
    // Sans elle Java dit "must implement abstract method loadUserByUsername(String)".
    // Spring Security l'appelle automatiquement à chaque tentative de connexion.
    @Override
    public UserDetails loadUserByUsername(String telephone) throws UsernameNotFoundException {
        User user = userRepository.findByTelephone(telephone)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Aucun utilisateur avec le téléphone : " + telephone));

        return new org.springframework.security.core.userdetails.User(
                user.getTelephone(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }
}
