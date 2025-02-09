package com.example.Schedule.service;

import com.example.Schedule.dto.ScheduleRequestDto;
import com.example.Schedule.dto.ScheduleResponseDto;
import com.example.Schedule.entity.Schedule;
import com.example.Schedule.entity.User;
import com.example.Schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    // create -> 일정 생성
    public ScheduleResponseDto createSchedule(User currentUser, String password, String title, String contents) {
        Schedule schedule = new Schedule(currentUser, password, title, contents);
        Schedule savedSchedule = scheduleRepository.save(schedule);
        return new ScheduleResponseDto(savedSchedule.getId(),
                savedSchedule.getUser().getUsername(),
                savedSchedule.getTitle(),
                savedSchedule.getContents());
    }

    // Read -> 일정 조회
    // 일정 아이디, 일정 제목, 사용자 아이디, 사용자 이름으로 조회 가능
    public List<ScheduleResponseDto> findSchedules(Long scheduleId, String title, Long userId, String username) {
        List<Schedule> scheduleList = scheduleRepository.findSchdules(
                scheduleId,
                sanitizeString(title),
                userId,
                sanitizeString(username)
        );
        if(scheduleList.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return scheduleList.stream().map(ScheduleResponseDto::toDto).toList();
    }

    // Updated -> 일정 수정
    // 비밀번호가 동일하다면, 비밀번호, 제목, 내용 수정 가능
    @Transactional
    public ScheduleResponseDto updateSchedule(Long id, String inputPassword, ScheduleRequestDto requestDto) {
        Schedule findSchedule = scheduleRepository.findByIdOrElseThrow(id);

        // 비밀번호 검증
        if(!inputPassword.equals(findSchedule.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        if(sanitizeString(requestDto.getPassword()) != null){
            findSchedule.setPassword(requestDto.getPassword());
        }

        if(sanitizeString(requestDto.getTitle()) != null){
            findSchedule.setTitle(requestDto.getTitle());
        }

        if(sanitizeString(requestDto.getContents()) != null){
            findSchedule.setContents(requestDto.getContents());
        }

        return ScheduleResponseDto.toDto(findSchedule);
    }

    // 공백 제거 및 빈 문자열을 null로 변환하는 메서드
    private String sanitizeString(String input) {
        return (input != null && !input.trim().isEmpty()) ? input : null;
    }
}
