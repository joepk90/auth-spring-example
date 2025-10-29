package com.springauthapi.authservice.database;
import org.springframework.stereotype.Repository;

import com.springauthapi.authservice.user.User;
import com.springauthapi.authservice.user.UserRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
// @Profile("fake") // Only active when "fake" profile is set
public class InMemoryUserRepository implements UserRepository {

    private final Map<Long, User> store = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @Override
    public <S extends User> S save(S entity) {
        if (entity.getId() == null) {
            entity.setId(idCounter.getAndIncrement());
        }
        store.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }

    // --- Unused JpaRepository methods ---
    @Override
    public boolean existsById(Long id) {
        return store.containsKey(id);
    }

    @Override
    public long count() {
        return store.size();
    }

    @Override
    public void delete(User entity) {
        store.remove(entity.getId());
    }

    @Override
    public void deleteAll() {
        store.clear();
    }

    @Override
    public <S extends User> List<S> saveAll(Iterable<S> entities) {
        List<S> list = new ArrayList<>();
        for (S e : entities)
            list.add(save(e));
        return list;
    }

    @Override
    public void deleteAll(Iterable<? extends User> entities) {
        entities.forEach(e -> store.remove(e.getId()));
    }

    // Implement deleteAllById required by CrudRepository
    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        ids.forEach(store::remove);
    }

    // Implement findByEmail and existsByEmail required by UserRepository
    @Override
    public Optional<User> findByEmail(String email) {
        if (email == null)
            return Optional.empty();
        return store.values().stream()
                .filter(u -> email.equals(u.getEmail()))
                .findFirst();
    }

    @Override
    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }

    // Optional: implement paging/sorting if you actually use those methods
    @Override
    public List<User> findAllById(Iterable<Long> ids) {
        return ((Collection<Long>) ids).stream()
                .map(store::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
