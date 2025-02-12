package com.example.Schedule.controller;

import com.example.Schedule.dto.comment.CommentResponseDto;
import com.example.Schedule.entity.User;
import com.example.Schedule.repository.UserRepository;
import com.example.Schedule.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("schedules")
@RequiredArgsConstructor
@Tag(name = "댓글 관리 API", description = "댓글을 관리하는 API입니다.")
public class CommentController {
    private final CommentService commentService;
    private final UserRepository userRepository;

    @PostMapping("{scheduleId}/comments")
    @Operation(summary = "댓글 생성", description = "댓글을 생성합니다.")
    public ResponseEntity<CommentResponseDto> addComment(
            @PathVariable Long scheduleId,
            @RequestParam String comment,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Long userId = (Long) session.getAttribute("userId");
        User user = userRepository.findByIdOrElseThrow(userId);
        CommentResponseDto commentResponseDto = commentService.addComment(user, scheduleId, comment);
        return new ResponseEntity<>(commentResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/comments")
    @Operation(summary = "댓글 조회", description = "특정 일정의 댓글을 조회합니다")
    public ResponseEntity<List<CommentResponseDto>> getComment(
            @RequestParam Long scheduleId
    ) {
        List<CommentResponseDto> comments = commentService.findComments(scheduleId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PatchMapping("/{scheduleId}/comments/{commentId}")
    @Operation(summary = "댓글 수정", description = "댓글을 수정합니다.")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Long scheduleId,
            @PathVariable Long commentId,
            @RequestParam String newComment,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Long userId = (Long) session.getAttribute("userId");
        CommentResponseDto updatedComment = commentService.updateComment(scheduleId, commentId, userId, newComment);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @DeleteMapping("/{scheduleId}/comments/{commentId}")
    @Operation(summary = "댓글 삭제", description = "특정 일정의 댓글을 삭제합니다.")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long scheduleId,
            @PathVariable Long commentId,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Long currentUserID = (Long) session.getAttribute("userId");
        commentService.deleteComment(commentId, currentUserID, scheduleId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
