package com.example.Schedule.dto;

import com.example.Schedule.entity.User;
import lombok.Getter;


@Getter
public class UserResponseDto {

    private final Long id;
    private final String username;
    private final String email;

    public UserResponseDto(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    // entity를 dto로 변환
    public static UserResponseDto toDto(User user) {
        return new UserResponseDto(user.getId(), user.getUsername(), user.getEmail());
    }

}