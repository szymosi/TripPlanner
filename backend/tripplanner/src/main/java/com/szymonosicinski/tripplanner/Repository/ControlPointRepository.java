package com.szymonosicinski.tripplanner.Repository;

import com.szymonosicinski.tripplanner.Entity.ControlPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ControlPointRepository  extends JpaRepository<ControlPoint, UUID> {
    List<ControlPoint> findAllByTrip_Id(UUID tripId);
}
