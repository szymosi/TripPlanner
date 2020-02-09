package com.szymonosicinski.tripplanner.Repository;

import com.szymonosicinski.tripplanner.Entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {

    Page<Comment> findAllByBlogEntryId(UUID id, Pageable pageable);

    Optional<Comment> findById(UUID id);
}
