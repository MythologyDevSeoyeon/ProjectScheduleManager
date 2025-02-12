package com.example.Schedule.dto.schedule;

import com.example.Schedule.entity.Schedule;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Getter
public class ScheduleResponseDto {

    private final Long id;
    private final String username;
    private final String title;
    private final String contents;
    private final String createdAt;
    private final String updatedAt;

    public ScheduleResponseDto(Long id, String username, String title, String contents, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.username = username;
        this.title = title;
        this.contents = contents;
        this.createdAt = formatDate(createdAt);
        this.updatedAt = formatDate(updatedAt);
    }

    // entity를 dto로 변환
    public static ScheduleResponseDto toDto(Schedule schedule) {
        return new ScheduleResponseDto(schedule.getId(), schedule.getUser().getUsername(), schedule.getTitle(), schedule.getContents(),schedule.getCreatedAt(),schedule.getUpdatedAt());
    }

    // 날짜 형식 변환
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static String formatDate(LocalDateTime datetime){
        return Optional.ofNullable(datetime)
                .map(FORMATTER::format)
                .orElse(null);
    }


}
