package com.example.Schedule.service;

import com.example.Schedule.dto.SignUpResponseDto;
import com.example.Schedule.dto.UserResponseDto;
import com.example.Schedule.entity.User;
import com.example.Schedule.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // Create -> 사용자 정보 생성
    public SignUpResponseDto signUp(String username, String password, String email) {
        User user = new User(username, password, email);
        User savedUser = userRepository.save(user);
        return new SignUpResponseDto(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }

    // Read
    // 아이디, 이름, 이메일로 조회
    public List<UserResponseDto> findUsers(Long id, String username, String email) {
        return userRepository.findUsers(id,
                        (username != null && !username.trim().isEmpty() ? username : null),
                        (email != null && !email.trim().isEmpty() ? email : null))
                .stream()
                .map(UserResponseDto::toDto)
                .toList();
    }


    //update
    // 비밀번호 일치 시, 비밀번호, 사용자 이름, 이메일 수정 가능
    @Transactional
    public SignUpResponseDto updateUser(Long id, String inputPassword, String newPassword, String newUsername, String newEmail) {

        User findUser = userRepository.findByIdOrElseThrow(id);

        // 비밀번호 검증
        if (!findUser.getPassword().equals(inputPassword)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호 변경
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            findUser.setPassword(newPassword);
        }

        // 사용자 이름
        if (newUsername != null && !newUsername.trim().isEmpty()) {
            findUser.setUsername(newUsername);
        }

        // 이메일 수정
        if (newEmail != null && !newEmail.trim().isEmpty()) {
            findUser.setEmail(newEmail);
        }

        return new SignUpResponseDto(findUser.getId(), findUser.getUsername(), findUser.getEmail());
    }

    //delete -> 논리 삭제 요청
    @Transactional
    public void softDeleteUser(Long id, String inputPassword) {
        User user = userRepository.findByIdOrElseThrow(id);

        // 비밀번호 검증
        if (!user.getPassword().equals(inputPassword)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        user.setDeleted(true);
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

    // 물리 삭제된 사용자 조회 --> 수정하기
    public List<UserResponseDto> getDeletedUser() {
        return userRepository.findAllByIsDeletedTrue().stream()
                .map(UserResponseDto::toDto)
                .toList();
    }

    // 물리 삭제된 사용자 복구 --> 수정하기
    public void restoreUser(Long id) {
        User user = userRepository.findByIdOrElseThrow(id);
        user.setDeleted(false);
        user.setDeletedAt(null);
        userRepository.save(user);
    }

}
