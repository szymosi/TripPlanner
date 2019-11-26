package com.szymonosicinski.tripplanner.Repository;

import com.szymonosicinski.tripplanner.Entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, UUID> {
}
