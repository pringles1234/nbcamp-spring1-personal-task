package com.sparta.schedule.dto;


import com.sparta.schedule.entity.Schedule;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class CheckListResponseDto {

    private Long schedule_id;
    private String user_name;
    private String event;
    private LocalDateTime updated_dateTime;

    public CheckListResponseDto(Long schedule_id, String user_name, String event, LocalDateTime getDateUntilDay) {
        this.schedule_id = schedule_id;
        this.user_name = user_name;
        this.event = event;
        this.updated_dateTime = getDateUntilDay;

    }

    public CheckListResponseDto(Schedule schedule) {
        this.schedule_id = schedule.getSchedule_id();
        this.user_name = schedule.getUser_name();
        this.event = schedule.getEvent();
        this.updated_dateTime = schedule.getUpdated_dateTime();
    }

    public LocalDate getDateUntilDay(LocalDateTime updated_dateTime) {
        return updated_dateTime.toLocalDate();
    }

}
