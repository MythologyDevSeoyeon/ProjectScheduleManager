package com.example.Schedule.service;

import com.example.Schedule.dto.comment.CommentResponseDto;
import com.example.Schedule.entity.Comment;
import com.example.Schedule.entity.Schedule;
import com.example.Schedule.entity.User;
import com.example.Schedule.repository.CommentRepository;
import com.example.Schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final ScheduleRepository scheduleRepository;
    private final CommentRepository commentRepository;

    // Create -> 댓글 생성
    public CommentResponseDto addComment(User user, Long scheduleId, String inputComment) {
        Schedule schedule = scheduleRepository.findByIdOrElseThrow(scheduleId);
        Comment comment = new Comment(user, schedule, inputComment);
        Comment savedComment = commentRepository.save(comment);
        return new CommentResponseDto(
                savedComment.getId(),
                savedComment.getSchedule().getId(),
                savedComment.getUser().getUsername(),
                savedComment.getComment(),
                savedComment.getCreatedAt(),
                savedComment.getUpdatedAt());
    }

    // Read -> 댓글 조회
    // 일정 아이디로 댓글을 조회
    public List<CommentResponseDto> findComments(Long scheduleId) {
        List<Comment> commentList = commentRepository.findCommentsByScheduleId(scheduleId);
        if (commentList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return commentList.stream().map(CommentResponseDto::toDto).toList();
    }

    // Update -> 댓글 수정
    @Transactional
    public CommentResponseDto updateComment(Long scheduleId, Long commentId, Long userId, String newComment) {
        Comment comment = commentRepository.findByIdOrElseThrow(commentId);
        if (!comment.getSchedule().getId().equals(scheduleId)) {
            throw new IllegalArgumentException("해당 댓글은 이 일정에 속하지 않습니다.");
        }
        verifyCommentOwnership(comment, userId);
        comment.setComment(newComment);
        Comment updatedComment = commentRepository.save(comment);
        return CommentResponseDto.toDto(updatedComment);
    }

    // Delete -> 댓글 삭제
    @Transactional
    public void deleteComment(Long id, Long currentUserId, Long scheduleId) {
        Comment comment = commentRepository.findByIdOrElseThrow(id);
        if (!comment.getSchedule().getId().equals(scheduleId)) {
            throw new IllegalArgumentException("해당 댓글은 이 일정에 속하지 않습니다.");
        }
        verifyCommentOwnership(comment, currentUserId);
        commentRepository.deleteById(id);
    }

    // 권한 검증 메소드
    private void verifyCommentOwnership(Comment comment, Long currentUserId) {
        if (!comment.getUser().getId().equals(currentUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }
    }


}
