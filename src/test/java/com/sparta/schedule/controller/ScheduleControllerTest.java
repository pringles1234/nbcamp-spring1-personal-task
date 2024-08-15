package com.sparta.schedule.controller;

import com.sparta.schedule.dto.CreateScheduleRequestDto;
import com.sparta.schedule.dto.CreateScheduleResponseDto;
import com.sparta.schedule.entity.Schedule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@SpringBootTest
class ScheduleControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void contextLoads() {

        CreateScheduleRequestDto createScheduleRequestDto = new CreateScheduleRequestDto();

        createScheduleRequestDto.setUser_name("sanghoon");
        createScheduleRequestDto.setPassword("1234");
        createScheduleRequestDto.setEvent("요것이 스케줄이댜");

        Schedule schedule = new Schedule(createScheduleRequestDto);

        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime updatedTime = LocalDateTime.now();

        //DB 저장
        KeyHolder keyHolder = new GeneratedKeyHolder(); // 기본 키를 반환받기 위한 객체

        String sql = "INSERT INTO schedule (user_name, password, event, created_date, updated_date) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update( con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, schedule.getUser_name());
                    preparedStatement.setString(2, schedule.getPassword());
                    preparedStatement.setString(3, schedule.getEvent());

                    preparedStatement.setTimestamp(4, Timestamp.valueOf(currentTime));
                    preparedStatement.setTimestamp(5, Timestamp.valueOf(updatedTime));
                    return preparedStatement;
                },
                keyHolder);

//        // DB Insert 후 받아온 기본키 확인
        Long id = keyHolder.getKey().longValue();
        schedule.setSchedule_id(id);
//
        // Entity -> CreatescheduleResponseDto
        CreateScheduleResponseDto createScheduleResponseDto = new CreateScheduleResponseDto(schedule);

    }
}