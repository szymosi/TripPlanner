package com.szymonosicinski.tripplanner.Repository;

import com.szymonosicinski.tripplanner.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findById(UUID uuid);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);
}
