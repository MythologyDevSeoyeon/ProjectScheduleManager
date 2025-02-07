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

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "일정 관리 API" ,description = "일정과 사용자를 생성하고 조회하는 API입니다.")
public class UserController {

    private final UserService userService;

    // Create
    @Operation(summary = "사용자 생성", description = "사용자를 생성합니다.")
    @PostMapping
    public ResponseEntity<SignUpResponseDto> signUp (@RequestBody SignUpRequestDto requestDto){
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
    @Operation(summary = "사용자 조회", description = "아이디, 사용자이름, 이메일로 사용자를 조회합니다.")
    public ResponseEntity<List<UserResponseDto>> findUsers (
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email
    ){
        List<UserResponseDto> userList = userService.findUsers(id, username, email);
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }


}
