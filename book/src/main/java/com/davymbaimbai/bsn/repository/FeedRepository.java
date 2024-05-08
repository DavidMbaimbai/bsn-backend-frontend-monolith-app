package com.davymbaimbai.bsn.repository;

import com.davymbaimbai.bsn.entity.FeedBack;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FeedRepository extends JpaRepository<FeedBack, Integer> {
    @Query("""
             SELECT feedback
             FROM FeedBack feedback
             WHERE feedback.book.id = :bookId
            """)
    Page<FeedBack> findAllByBookId(Integer bookId, Pageable pageable);
}
