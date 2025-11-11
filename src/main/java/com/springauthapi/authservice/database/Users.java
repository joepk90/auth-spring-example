package com.springauthapi.authservice.database;

import com.springauthapi.authservice.user.User;
import com.springauthapi.authservice.user.UserRepository;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.springauthapi.authservice.exceptions.DatabaseSeedingException;
import com.springauthapi.authservice.user.Role;

@Component
@AllArgsConstructor
public class Users {
        private final PasswordEncoder passwordEncoder;
        private final UserRepository userRepository;

        // Initialize with seeded users
        @jakarta.annotation.PostConstruct
        public void init() {
                for (User user : buildUsers()) {
                        userRepository.save(user);
                }
        }

        public List<User> buildUsers() {
                try {
                        List<User> users = new ArrayList<>();

                        // userId = 1
                        users.add(User.builder()
                                        .name("User")
                                        .email("user@gmail.com")
                                        .role(Role.USER)
                                        .password(passwordEncoder.encode("user"))
                                        .build());

                        // userId = 2
                        users.add(User.builder()
                                        .name("Agent")
                                        .email("agent@gmail.com")
                                        .role(Role.AGENT)
                                        .password(passwordEncoder.encode("agent"))
                                        .build());
                                        
                        // userId = 3
                        users.add(User.builder()
                                        .name("Manager")
                                        .email("manager@gmail.com")
                                        .role(Role.MANAGER)
                                        .password(passwordEncoder.encode("manager"))
                                        .build());

                        // userId = 4
                        users.add(User.builder()
                                        .name("Admin")
                                        .email("admin@gmail.com")
                                        .role(Role.ADMIN)
                                        .password(passwordEncoder.encode("admin"))
                                        .build());

                        // userId = 5
                        users.add(User.builder()
                                        .name("Owner")
                                        .email("owner@gmail.com")
                                        .role(Role.OWNER)
                                        .password(passwordEncoder.encode("owner"))
                                        .build());

                        return users;

                } catch (Exception e) {
                        throw new DatabaseSeedingException("Failed to generate user data");
                }
        }
}
