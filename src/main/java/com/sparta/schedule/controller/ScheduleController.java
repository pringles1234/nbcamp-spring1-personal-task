package com.sparta.schedule.controller;

import com.sparta.schedule.dto.*;
import com.sparta.schedule.entity.Schedule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ScheduleController {

    private final JdbcTemplate jdbcTemplate;

    public ScheduleController(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/schedule")
    public CreateScheduleResponseDto createSchedule(@RequestBody CreateScheduleRequestDto createScheduleRequestDto){

        //CreateScheduleRequestDto -> Entity
        Schedule schedule = new Schedule(createScheduleRequestDto);

        //DB 저장
        KeyHolder keyHolder = new GeneratedKeyHolder(); // 기본 키를 반환받기 위한 객체

        String sql = "INSERT INTO schedule (user_name, password, event, created_date, updated_date) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update( con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, schedule.getUser_name());
                    preparedStatement.setString(2, schedule.getPassword());
                    preparedStatement.setString(3, schedule.getEvent());

                    preparedStatement.setTimestamp(4, Timestamp.valueOf(schedule.getCreated_dateTime()));
                    preparedStatement.setTimestamp(5, Timestamp.valueOf(schedule.getUpdated_dateTime()));
                    return preparedStatement;
                },
                keyHolder);

//        // DB Insert 후 받아온 기본키 확인
        Long id = keyHolder.getKey().longValue();
        schedule.setSchedule_id(id);
//
        // Entity -> CreatescheduleResponseDto
        CreateScheduleResponseDto createScheduleResponseDto = new CreateScheduleResponseDto(schedule);

        return createScheduleResponseDto;
    }



    @GetMapping("/schedule")
    public List<CheckListResponseDto> getScheduleAll(){
        // DB 조회
        String sql = "SELECT * FROM schedule";

        return jdbcTemplate.query(sql, new RowMapper<CheckListResponseDto>() {
            @Override
            public CheckListResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                // SQL 의 결과로 받아온 Memo 데이터들을 CheckListResponseDto 타입으로 변환해줄 메서드
                Long schedule_id = rs.getLong("schedule_id");
                String user_name = rs.getString("user_name");
                String event = rs.getString("event");
                LocalDateTime updated_dateTime = rs.getTimestamp("updated_date").toLocalDateTime();
                LocalDate updated_date = updated_dateTime.toLocalDate();
                return new CheckListResponseDto(schedule_id, user_name, event, updated_date);
            }
        });
    }

    @PutMapping("/schedule/{id}")
    public String updateSchedule(@PathVariable(value="id") Long schedule_id, @RequestBody ModifyScheduleRequestDto modifyrequestDto){
        //해당 메모가 DB에 존재하는지 확인
        Schedule schedule = findById(schedule_id);

        if (schedule != null) {
            // 요청된 비밀번호와 DB의 비밀번호가 일치하는지 확인
            if (schedule.getPassword().equals(modifyrequestDto.getPassword())) {
                // 비밀번호가 일치할 경우 메모 내용 수정
                String sql = "UPDATE schedule SET user_name = ?, event = ? WHERE schedule_id = ?";
                jdbcTemplate.update(sql, modifyrequestDto.getUser_name(), modifyrequestDto.getEvent(), schedule_id);
                return String.valueOf(schedule_id);
            } else {
                return "비밀번호가 일치하지 않습니다.";
//                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
        } else {
            return "선택한 일정은 존재하지 않습니다.";
//            throw new IllegalArgumentException("선택한 일정은 존재하지 않습니다.");
        }
    }

    @DeleteMapping("/schedule/{id}")
    public String deleteMemo(@PathVariable(value="id")Long schedule_id, @RequestBody DeleteScheduleRequestDto deleterequestDto) {
        // 해당 메모가 DB에 존재하는지 확인
        Schedule schedule = findById(schedule_id);
        if(schedule != null) {
            // 요청된 비밀번호와 DB의 비밀번호가 일치하는지 확인
            if(schedule.getPassword().equals(deleterequestDto.getPassword())){
                String sql = "DELETE FROM schedule WHERE schedule_id = ?";
                jdbcTemplate.update(sql, schedule_id);
                return String.valueOf(schedule_id);
            }else{
                return "비밀번호가 일치하지 않습니다.";
                //throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
        } else {
            return "선택한 일정은 존재하지 않습니다.";
            //throw new IllegalArgumentException("선택한 일정은 존재하지 않습니다.");
        }
    }

    private Schedule findById(Long schedule_id) {
        // DB 조회
        String sql = "SELECT * FROM schedule WHERE schedule_id = ?";

        return jdbcTemplate.query(sql, resultSet -> {
            if(resultSet.next()) {
                Schedule schedule = new Schedule();
                schedule.setUser_name(resultSet.getString("user_name"));
                schedule.setPassword(resultSet.getString("password"));
                schedule.setEvent(resultSet.getString("event"));
                return schedule;
            } else {
                return null;
            }
        }, schedule_id);
    }



}
