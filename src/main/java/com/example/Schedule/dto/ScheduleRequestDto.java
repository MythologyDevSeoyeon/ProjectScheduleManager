package com.example.Schedule.dto;

import lombok.Getter;

@Getter
public class ScheduleRequestDto {

    private final String password;
    private final String title;
    private final String contents;

    public ScheduleRequestDto(String password, String title, String contents) {
        this.password = password;
        this.title = title;
        this.contents = contents;
    }
}
