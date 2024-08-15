package com.sparta.schedule.controller;

import com.sparta.schedule.dto.CheckListResponseDto;
import com.sparta.schedule.dto.CreateScheduleRequestDto;
import com.sparta.schedule.dto.CreateScheduleResponseDto;
import com.sparta.schedule.entity.Schedule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

        String sql = "INSERT INTO schedule (user_name, password, event) VALUES (?, ?, ?)";
        jdbcTemplate.update( con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, schedule.getUser_name());
                    preparedStatement.setString(2, schedule.getPassword());
                    preparedStatement.setString(3, schedule.getEvent());
                    return preparedStatement;
                },
                keyHolder);

        // DB Insert 후 받아온 기본키 확인
        Long id = keyHolder.getKey().longValue();
        schedule.setUser_id(id);

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
                Long user_id = rs.getLong("user_id");
                String user_name = rs.getString("user_name");
                String event = rs.getString("event");
                LocalDateTime updated_dateTime = rs.getTimestamp("updated_date").toLocalDateTime();
                LocalDate updated_date = updated_dateTime.toLocalDate();
                return new CheckListResponseDto(user_id, user_name, event, updated_date);
            }
        });
    }

}
