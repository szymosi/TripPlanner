package com.szymonosicinski.tripplanner.Repository;

import com.szymonosicinski.tripplanner.DTO.UserDTO.UserDTO;
import com.szymonosicinski.tripplanner.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findById(UUID uuid);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    @Query(value = "select * from userr u inner join participants p on p.userid=u.id where p.tripid= :tripId", nativeQuery = true)
    Page<User> findAllParticipantsByTrip(@Param("tripId") UUID tripId, Pageable pageable);
}
