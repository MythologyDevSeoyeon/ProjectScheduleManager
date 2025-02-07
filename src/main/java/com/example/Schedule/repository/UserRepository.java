package com.example.Schedule.repository;

import com.example.Schedule.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    // 조회
    @Query ("SELECT u From User u WHERE " +
            "(:id IS NULL OR u.id = :id) AND " +
            "(:username IS NULL OR u.username = :username) AND " +
            "(:email IS NULL OR u.email = :email)")
    List<User> findUsers (
            @Param("id") Long id,
            @Param("username") String username,
            @Param("email") String email
    );
}
