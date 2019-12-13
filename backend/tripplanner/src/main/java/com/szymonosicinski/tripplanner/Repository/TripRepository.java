package com.szymonosicinski.tripplanner.Repository;

import com.szymonosicinski.tripplanner.Entity.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TripRepository extends JpaRepository<Trip, UUID> {

    Optional<Trip> findById(UUID id);

    Page<Trip> findAllByOrganizer(UUID organizerId, Pageable pageable);

    @Query(value = "select * from trip t inner join participants p on t.id=p.trip where p.user= :userId", nativeQuery = true)
    Page<Trip> findAllByParticipant(@Param("userId") UUID userId, Pageable pageable);

    @Query(value = "insert into participants (tripid,userid) values (:tripId, :userId)", nativeQuery = true)
    void saveParticipant(@Param("tripId") UUID tripId, @Param("userId") UUID userId);

    @Query(value="select count(1) from participants p where p.trip= :tripId and p.user= :userId", nativeQuery = true)
    int isParticipant(@Param("tripId") UUID tripId, @Param("userId") UUID userId);
}
