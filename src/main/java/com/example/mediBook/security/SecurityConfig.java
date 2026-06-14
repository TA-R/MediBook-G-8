package com.example.mediBook.security;

import com.example.mediBook.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .authorizeHttpRequests(auth -> auth
                        // Pages publiques
                        .requestMatchers(
                                "/admin/login",
                                "/admin/doLogin",
                                "/authPatient/login",
                                "/authPatient/doLogin",
                                "/authPatient/register",
                                "/h2-console/**",
                                "/css/**", "/js/**", "/images/**"
                        ).permitAll()

                        // Pages admin → ROLE_ADMIN obligatoire
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")

                        // Pages médecin
                        .requestMatchers("/medecin/**").hasAuthority("ROLE_MEDECIN")

                        // Pages patient
                        .requestMatchers("/patient/**").hasAuthority("ROLE_PATIENT")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/admin/login")
                        .loginProcessingUrl("/admin/doLogin")
                        .defaultSuccessUrl("/admin/dashboard", false)
                        .failureUrl("/admin/login?error=true")
                        .permitAll()
                )
                // ✅ Gérer les accès refusés (403)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            String uri = request.getRequestURI();
                            if (uri.startsWith("/admin")) {
                                response.sendRedirect("/admin/login");
                            } else if (uri.startsWith("/patient") || uri.startsWith("/authPatient")) {
                                response.sendRedirect("/authPatient/login");
                            } else {
                                response.sendRedirect("/admin/login");
                            }
                        })
                )
                .logout(logout -> logout
                        .logoutUrl("/admin/logout")           // ✅ URL du logout
                        .logoutSuccessUrl("/admin/login")     // ✅ Redirection après logout
                        .invalidateHttpSession(true)          // ✅ Détruire la session
                        .clearAuthentication(true)            // ✅ Effacer l'authentification
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            com.example.mediBook.models.User user = userRepository
                    .findByTelephone(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

            String role = user.getRole();
            // ✅ Éviter le double préfixe ROLE_ROLE_ADMIN
            String authority = role.startsWith("ROLE_") ? role : "ROLE_" + role;

            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getTelephone())
                    .password(user.getPassword())
                    .authorities(authority)
                    .build();
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}