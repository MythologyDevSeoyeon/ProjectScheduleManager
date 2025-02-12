package com.example.Schedule.controller;

import com.example.Schedule.dto.user.UserRequestDto;
import com.example.Schedule.dto.user.UserResponseDto;
import com.example.Schedule.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "사용자 관리 API", description = "사용자를 관리하는 API입니다.")
public class UserController {

    private final UserService userService;

    // Create -> 사용자 정보 생성
    @PostMapping("/signup")
    @Operation(summary = "사용자 생성", description = "사용자를 생성합니다.")
    public ResponseEntity<UserResponseDto> signUp(@Valid @RequestBody UserRequestDto requestDto) {
        UserResponseDto signUpResponseDto = userService.signUp(
                requestDto.getUsername(),
                requestDto.getPassword(),
                requestDto.getEmail()
        );
        return new ResponseEntity<>(signUpResponseDto, HttpStatus.CREATED);
    }

    // Read
    // 아이디, 이름, 이메일로 조회
    @GetMapping
    @Operation(summary = "사용자 조회", description = "아이디, 사용자 이름, 이메일로 사용자를 조회합니다.")
    public ResponseEntity<List<UserResponseDto>> findUsers(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email
    ) {
        List<UserResponseDto> userList = userService.findUsers(id, username, email);
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    //update
    // 비밀번호 일치 시, 사용자 이름, 비밀번호, 이메일 수정 가능
    @PatchMapping("/my")
    @Operation(summary = "사용자 수정", description = "로그인한 사용자의 이름, 비밀번호, 이메일을 수정합니다.")
    public ResponseEntity<UserResponseDto> updateUser(
            @RequestParam String inputPassword,
            @RequestParam(required = false) String newPassword,
            @RequestParam(required = false) String newUsername,
            @RequestParam(required = false) String newEmail,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "권한이 없습니다.");
        }
        Long currentUserId = (Long) session.getAttribute("userId");
        UserResponseDto responseDto = userService.updateUser(currentUserId, inputPassword, newPassword, newUsername, newEmail);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // delete -> 논리 삭제 요청
    @DeleteMapping("/my")
    @Operation(summary = "사용자 삭제", description = "비밀번호 일치 시 사용자를 삭제합니다.")
    public ResponseEntity<Void> deleteUser(@RequestParam String password, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "권한이 없습니다.");
        }
        Long currentUserId = (Long) session.getAttribute("userId");
        userService.softDeleteUser(currentUserId, password);
        session.invalidate(); // 로그아웃 처리
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 논리 삭제된 사용자 조회 -> 관리자만
    @GetMapping("/deleted")
    @Operation(summary = "삭제된 사용자 조회", description = "삭제된 사용자를 조회합니다.")
    public ResponseEntity<List<UserResponseDto>> getDeletedUser(HttpServletRequest request) {
        checkAdminRole(request);
        List<UserResponseDto> deletedUsers = userService.getDeletedUsers();
        return new ResponseEntity<>(deletedUsers, HttpStatus.OK);
    }

    // 논리 삭제된 사용자 복구 -> 관리자만
    @PutMapping("/restore/{id}")
    @Operation(summary = "삭제된 사용자 복구", description = "삭제된 사용자를 복구합니다.")
    public ResponseEntity<Void> restoreUser(@PathVariable Long id, HttpServletRequest request) {
        checkAdminRole(request);
        userService.restoreUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 관리자 권한 검증 메소드
    private void checkAdminRole(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("role"))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.");
        }
    }
}
