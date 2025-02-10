package com.example.Schedule.service;

import com.example.Schedule.entity.User;
import com.example.Schedule.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserRepository userRepository;

    public void login(String email, String password, HttpServletRequest request) {
        User user = userRepository.findByEmailOrElseThrow(email);

        if(!user.getPassword().equals(password)){
            throw new IllegalArgumentException("Invalid email or password");
        }

        HttpSession session = request.getSession();
        session.setAttribute("userId", user.getId());
    }
}
