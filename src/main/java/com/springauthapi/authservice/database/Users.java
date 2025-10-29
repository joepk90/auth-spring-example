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

                        users.add(User.builder()
                                        .name("John Smith")
                                        .email("johnsmith@gmail.com")
                                        .role(Role.USER)
                                        .password(passwordEncoder.encode("userpass"))
                                        .build());

                        users.add(User.builder()
                                        .name("John Doe")
                                        .email("johndoe@gmail.com")
                                        .role(Role.AGENT)
                                        .password(passwordEncoder.encode("agentpass"))
                                        .build());

                        users.add(User.builder()
                                        .name("Jane Smith")
                                        .email("janesmith@gmail.com")
                                        .role(Role.MANAGER)
                                        .password(passwordEncoder.encode("managerpass"))
                                        .build());

                        users.add(User.builder()
                                        .name("Jane Doe")
                                        .email("janedoeh@gmail.com")
                                        .role(Role.ADMIN)
                                        .password(passwordEncoder.encode("adminpass"))
                                        .build());

                        users.add(User.builder()
                                        .name("Charles Montgomery Burns")
                                        .email("charlesburns@gmail.com")
                                        .role(Role.OWNER)
                                        .password(passwordEncoder.encode("ownerpass"))
                                        .build());

                        return users;

                } catch (Exception e) {
                        throw new DatabaseSeedingException("Failed to generate user data");
                }
        }
}
