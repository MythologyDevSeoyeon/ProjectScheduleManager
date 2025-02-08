package com.example.Schedule.dto;

import lombok.Getter;

@Getter
public class UserRequestDto {

    private final String username;
    private final String email;
    private final String password;

    public UserRequestDto(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
