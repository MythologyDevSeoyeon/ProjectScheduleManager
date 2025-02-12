package com.example.Schedule.repository;

import com.example.Schedule.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    // Read
    // 아이디, 이름, 이메일로 조회
    @Query("SELECT u From User u WHERE " +
            "(:id IS NULL OR u.id = :id) AND " +
            "(:username IS NULL OR u.username = :username) AND " +
            "(:email IS NULL OR u.email = :email)")
    List<User> findUsers(
            @Param("id") Long id,
            @Param("username") String username,
            @Param("email") String email
    );

    // Read
    // 단일 아이디 조회
    default User findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id));
    }

    // Read
    // 이메일로 조회
    Optional<User> findByEmail(String email);

    default User findByEmailOrElseThrow(String email) {
        return findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist email = " + email));
    }

    // Delete
    // 삭제 요청 후 2주가 지난 사용자만 삭제
    List<User> findAllByIsDeletedTrueAndDeletedAtBefore(LocalDateTime dateTime);

    // 삭제 요청된 사용자 조회
    @Query(value = "SELECT * FROM user WHERE is_deleted = true", nativeQuery = true)
    List<User> findAllByIsDeletedTrue();

}
