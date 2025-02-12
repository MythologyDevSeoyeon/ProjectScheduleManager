package com.example.Schedule.dto.comment;

import com.example.Schedule.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Getter
public class CommentResponseDto {

    private final String username;
    private final String comment;
    private final Long id;
    private final Long scheduleId;
    private final String createdAt;
    private final String updatedAt;


    public CommentResponseDto(Long id, Long scheduleId, String username, String comment, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.username = username;
        this.scheduleId = scheduleId;
        this.comment = comment;
        this.id = id;
        this.createdAt = formatDate(createdAt);
        this.updatedAt = formatDate(updatedAt);
    }

    // 엔티티를 dto로 변환
    public static CommentResponseDto toDto(Comment comment) {
        return new CommentResponseDto(comment.getId(), comment.getSchedule().getId(), comment.getUser().getUsername(), comment.getComment(), comment.getCreatedAt(), comment.getUpdatedAt());
    }

    // 날짜 형식 변환
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static String formatDate(LocalDateTime datetime) {
        return Optional.ofNullable(datetime)
                .map(FORMATTER::format)
                .orElse(null);
    }
}
