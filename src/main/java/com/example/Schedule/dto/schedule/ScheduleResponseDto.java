package com.example.Schedule.dto.schedule;

import com.example.Schedule.entity.Schedule;
import lombok.Getter;

@Getter
public class ScheduleResponseDto {

    private final Long id;
    private final String username;
    private final String title;
    private final String contents;

    public ScheduleResponseDto(Long id, String username, String title, String contents) {
        this.id = id;
        this.username = username;
        this.title = title;
        this.contents = contents;
    }

    // entity를 dto로 변환
    public static ScheduleResponseDto toDto (Schedule schedule){
        return new ScheduleResponseDto(schedule.getId(), schedule.getUser().getUsername(), schedule.getTitle(),schedule.getContents());
    }
}
