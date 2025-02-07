package com.example.Schedule.service;

import com.example.Schedule.dto.SignUpResponseDto;
import com.example.Schedule.dto.UserResponseDto;
import com.example.Schedule.entity.User;
import com.example.Schedule.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public SignUpResponseDto signUp(String username, String password, String email) {
        User user = new User(username, password, email);
        User savedUser = userRepository.save(user);
        return new SignUpResponseDto(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }

    public List<UserResponseDto> findUsers(Long id, String username, String email) {
        return userRepository.findUsers(id, username, email)
                .stream()
                .map(UserResponseDto::toDto)
                .toList();
    }
}
