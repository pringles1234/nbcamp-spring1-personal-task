package com.sparta.schedule.dto;

import com.sparta.schedule.entity.Schedule;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateScheduleResponseDto {

    private Long user_id;
    private String user_name;
    private String event;
    private LocalDateTime created_date;
    private LocalDateTime updated_date;

    public CreateScheduleResponseDto(Schedule schedule) {
        this.user_id = schedule.getUser_id();
        this.user_name = schedule.getUser_name();
        this.event = schedule.getEvent();
        this.created_date = schedule.getCreated_dateTime();
        this.updated_date = schedule.getUpdated_dateTime();
    }
}


