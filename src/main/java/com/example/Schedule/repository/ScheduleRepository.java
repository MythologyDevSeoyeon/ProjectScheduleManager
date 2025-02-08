package com.example.Schedule.repository;

import com.example.Schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository <Schedule, Long> {

    @Query("SELECT s FROM Schedule s " +
            "JOIN s.user u " +
            "WHERE (:scheduleId IS NULL OR s.id = :scheduleId) AND " +
            "(:title IS NULL OR s.title = :title) AND " +
            "(:userId IS NULL OR u.id = :userId) AND " +
            "(:username IS NULL OR u.username = :username)")
    List<Schedule> findSchdules(
            @Param("scheduleId") Long scheduleId,
            @Param("title") String title,
            @Param("userId") Long userId,
            @Param("username") String username
    );
}
