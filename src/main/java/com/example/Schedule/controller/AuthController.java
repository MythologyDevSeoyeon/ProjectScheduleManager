package com.example.Schedule.controller;

import com.example.Schedule.dto.login.LoginRequestDto;
import com.example.Schedule.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "로그인/로그아웃 API", description = "아이디와 비밀번호를 입력하여 로그인/로그아웃하는 API입니다.")
public class AuthController {
    private final LoginService loginService;

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "이메일과 비밀번호를 입력하여 로그인 합니다.")
    public ResponseEntity<String> login(
            @RequestBody LoginRequestDto requestDto,
            HttpServletRequest request
    ) {
        loginService.login(requestDto.getEmail(),requestDto.getPassword(), request);
        return ResponseEntity.ok("Login successful");
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "로그아웃 합니다.")
    public ResponseEntity<String> logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session != null){
            session.invalidate();
        }
        return ResponseEntity.ok("Logout successful");
    }

}
