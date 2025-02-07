package com.example.Schedule.controller;


import com.example.Schedule.dto.SignUpRequestDto;
import com.example.Schedule.dto.SignUpResponseDto;
import com.example.Schedule.dto.UserResponseDto;
import com.example.Schedule.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "일정 관리 API", description = "일정과 사용자를 생성하고 조회하는 API입니다.")
public class UserController {

    private final UserService userService;

    // Create -> 사용자 정보 생성
    @Operation(summary = "사용자 생성", description = "사용자를 생성합니다.")
    @PostMapping
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody SignUpRequestDto requestDto) {
        SignUpResponseDto signUpResponseDto =
                userService.signUp(
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
        if (userList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "조건에 맞는 사용자가 없습니다.");
        }
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    //update
    // 비밀번호 일치 시, 사용자 이름, 비밀번호, 이메일 수정 가능
    @PatchMapping("/{id}")
    @Operation(summary = "사용자 수정", description = "비밀번호 일치 시, 사용자 이름, 비밀번호, 이메일을 수정합니다.")
    public ResponseEntity<SignUpResponseDto> updateUser(
            @PathVariable Long id,
            @RequestParam String inputPassword,
            @RequestParam(required = false) String newPassword,
            @RequestParam(required = false) String newUsername,
            @RequestParam(required = false) String newEmail
    ) {
        SignUpResponseDto responseDto = userService.updateUser(id, inputPassword, newPassword, newUsername, newEmail);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // delete -> 논리 삭제 요청
    @DeleteMapping("/{id}")
    @Operation(summary = "사용자 삭제", description = "비밀번호 일치 시 사용자를 삭제합니다.")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id, @RequestParam String password) {
        userService.softDeleteUser(id, password);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 논리 삭제된 사용자 조회
    @GetMapping("/deleted")
    @Operation(summary = "삭제된 사용자 조회", description = "삭제된 사용자를 조회합니다.")
    public ResponseEntity<List<UserResponseDto>> getDeletedUser() {
        List<UserResponseDto> deletedUser = userService.getDeletedUser();
        return new ResponseEntity<>(deletedUser, HttpStatus.OK);
    }

    // 논리 삭제된 사용자 복구
    @PutMapping("/restore/{id}")
    @Operation(summary = "삭제된 사용자 복구", description = "삭제된 사용자를 복구합니다.")
    public ResponseEntity<Void> restoreUser(@PathVariable Long id){
        userService.restoreUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
