package com.example.Schedule.controller;

import com.example.Schedule.dto.schedule.ScheduleRequestDto;
import com.example.Schedule.dto.schedule.ScheduleResponseDto;
import com.example.Schedule.entity.User;
import com.example.Schedule.repository.UserRepository;
import com.example.Schedule.service.ScheduleService;
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
@RequestMapping("/schedules")
@RequiredArgsConstructor
@Tag(name = "일정 관리 API", description = "일정을 관리하는 API입니다.")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final UserRepository userRepository;

    @PostMapping
    @Operation(summary = "일정 생성", description = "일정을 생성합니다.")
    public ResponseEntity<ScheduleResponseDto> createSchedule(
            @RequestBody ScheduleRequestDto requestDto,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Long userId = (Long) session.getAttribute("userId");
        User user = userRepository.findByIdOrElseThrow(userId);
        ScheduleResponseDto schedule = scheduleService.createSchedule(
                user,
                requestDto.getPassword(),
                requestDto.getTitle(),
                requestDto.getContents()
        );
        return new ResponseEntity<>(schedule, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "일정 조회", description = "일정 아이디, 일정 제목, 사용자 아이디, 사용자 이름으로 일정을 조회합니다.")
    public ResponseEntity<List<ScheduleResponseDto>> findSchedules(
            @RequestParam(required = false) Long scheduleId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String username
    ) {
        List<ScheduleResponseDto> schedules = scheduleService.findSchedules(scheduleId, title, userId, username);
        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "일정 수정", description = "로그인한 사용자의 이름, 비밀번호, 이메일을 수정합니다.")
    public ResponseEntity<ScheduleResponseDto> updatedSchedule(
            @PathVariable Long id,
            @RequestParam String inputPassword,
            @RequestBody ScheduleRequestDto requestDto,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Long currentUserId = (Long) session.getAttribute("userId");
        ScheduleResponseDto scheduleResponseDto = scheduleService.updateSchedule(
                id, inputPassword, requestDto, currentUserId);
        return new ResponseEntity<>(scheduleResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "일정 삭제", description = "비밀번호 일치 시 일정 삭제")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable Long id,
            @RequestParam String password,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Long currentUserID = (Long) session.getAttribute("userId");
        scheduleService.deleteSchedule(id, password, currentUserID);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
