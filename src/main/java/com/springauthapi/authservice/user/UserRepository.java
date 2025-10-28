package com.springauthapi.authservice.user;

import java.util.Optional;

// import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * UserRepository:
 * 
 * - Uses a JpaRepository instead of CrudRepository so that the findAll method
 * returns
 * a List, instead of an Iterable object.
 * - Returning a List allows us to use the stream api to map User objects to the
 * UserDto object.
 */

 // CrudRepository used so that the InMemoryUserRepository can be implemented
public interface UserRepository extends CrudRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
