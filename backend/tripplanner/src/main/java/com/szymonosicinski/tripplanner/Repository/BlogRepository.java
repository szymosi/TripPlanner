package com.szymonosicinski.tripplanner.Repository;

import com.szymonosicinski.tripplanner.Entity.Blog;
import com.szymonosicinski.tripplanner.Entity.Budget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface BlogRepository extends JpaRepository<Blog, UUID> {

    Optional<Blog> findById(UUID blogId);

    Set<Blog> findAllByVisibility(Blog.Visibility visibility);

    Page<Blog> findAllByVisibility(Blog.Visibility visibility, Pageable pageable);

    Set<Blog> findAllByVisibilityIsNot(Blog.Visibility visibility);

    Set<Blog> findAllByVisibilityAndTripId(Blog.Visibility visibility, UUID tripId);
}
