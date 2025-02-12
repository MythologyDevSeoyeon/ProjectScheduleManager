package com.example.Schedule.repository;

import com.example.Schedule.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 댓글 조회
    @Query("SELECT c FROM Comment c " +
            "JOIN c.schedule s " +
            "WHERE s.id = :scheduleId"
    )
    List<Comment> findCommentsByScheduleId(@Param("scheduleId") Long scheduleId);

    default Comment findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id));
    }
}
