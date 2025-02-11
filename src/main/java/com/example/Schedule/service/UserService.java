package com.example.Schedule.service;

import com.example.Schedule.config.PasswordEncoder;
import com.example.Schedule.dto.UserResponseDto;
import com.example.Schedule.entity.User;
import com.example.Schedule.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EntityManager entityManager;
    private final PasswordEncoder passwordEncoder;

    // Create -> 사용자 정보 생성
    public UserResponseDto signUp(String username, String password, String email) {
        try {
            userRepository.findByEmailOrElseThrow(email);  // 이메일이 존재하면 예외 발생
            throw new IllegalArgumentException("Email is already registered");
        } catch (ResponseStatusException e) {
            // 이메일이 없으면 정상적으로 회원가입 진행
        }
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(username, encodedPassword, email);
        User savedUser = userRepository.save(user);
        return new UserResponseDto(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }

    // Read
    // 아이디, 이름, 이메일로 조회
    @Transactional
    public List<UserResponseDto> findUsers(Long id, String username, String email) {
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("deletedFilter").setParameter("isDeleted", false);

        List<User> userList = userRepository.findUsers(id,
                sanitizeString(username),
                sanitizeString(email)
        );

        if (userList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return userList.stream().map(UserResponseDto::toDto).toList();
    }


    //update
    // 비밀번호 일치 시, 비밀번호, 사용자 이름, 이메일 수정 가능
    @Transactional
    public UserResponseDto updateUser(Long id, String inputPassword, String newPassword, String newUsername, String newEmail) {
        User findUser = userRepository.findByIdOrElseThrow(id);
        validatePassword(inputPassword, findUser.getPassword());

        // 비밀번호 변경
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            findUser.setPassword(passwordEncoder.encode(newPassword));
        }

        if (sanitizeString(newPassword) != null) {
            findUser.setPassword(passwordEncoder.encode(newPassword));
        }
        if (sanitizeString(newEmail) != null) {
            findUser.setEmail(newEmail);
        }
        if (sanitizeString(newUsername) != null) {
            findUser.setUsername(newUsername);
        }

        return new UserResponseDto(findUser.getId(), findUser.getUsername(), findUser.getEmail());
    }

    //delete -> 논리 삭제 요청
    @Transactional
    public void softDeleteUser(Long id, String inputPassword) {
        User user = userRepository.findByIdOrElseThrow(id);
        validatePassword(inputPassword, user.getPassword());
        // 논리 삭제 처리
        user.setIsDeleted(true);
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    // delete -> 일정 시간이 지나면 물리 삭제
    @Transactional
    @Scheduled(cron = "0 0 3 * * ?")
    public void deleteUsers() {

        LocalDateTime twoWeeksAgo = LocalDateTime.now().minusWeeks(2);
        List<User> usersToDelete = userRepository.findAllByIsDeletedTrueAndDeletedAtBefore(twoWeeksAgo);

        if (!usersToDelete.isEmpty()) {
            userRepository.deleteAll(usersToDelete);
        }
    }

    // 논리 삭제된 사용자 조회
    public List<UserResponseDto> getDeletedUsers() {
        return userRepository.findAllByIsDeletedTrue().stream()
                .map(UserResponseDto::toDto)
                .toList();
    }

    // 논리 삭제된 사용자 복구 --> 수정하기
    @Transactional
    public void restoreUser(Long id) {
        Optional<User> restoreUser = userRepository.findAllByIsDeletedTrue().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();

        User user = restoreUser.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id = " + id));

        user.setIsDeleted(false);
        user.setDeletedAt(null);
        userRepository.save(user);
    }

    // 공백 제거 및 빈 문자열을 null로 변환하는 헬퍼 메서드
    private String sanitizeString(String input) {
        return (input != null && !input.trim().isEmpty()) ? input : null;
    }

    // 비밀번호 검증 메소드
    private void validatePassword(String inputPassword, String encodedPassword) {
        if (!passwordEncoder.matches(inputPassword, encodedPassword)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }
    }

}
