package com.szymonosicinski.tripplanner.Repository;

import com.szymonosicinski.tripplanner.Entity.BlogEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BlogEntryRepository extends JpaRepository<BlogEntry, UUID> {

    Page<BlogEntry> findAllByBlogId(UUID blogId, Pageable pageable);

    Optional<BlogEntry> findById(UUID blogEntryId);
}
