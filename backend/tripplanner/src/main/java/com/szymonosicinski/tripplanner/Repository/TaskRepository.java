package com.szymonosicinski.tripplanner.Repository;

import com.szymonosicinski.tripplanner.Entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    Optional<Task> findById(UUID taskId);

    Page<Task> findByTrip_IdOrderByDeadlineAsc(UUID tripId, Pageable pageable);

    List<Task> findAllByTrip_IdOrderByDeadlineAsc(UUID tripId);
}
