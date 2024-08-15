package com.sparta.schedule.dto;


import com.sparta.schedule.entity.Schedule;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CheckListResponseDto {

    private Long user_id;
    private String user_name;
    private String event;
    private LocalDate updated_date;

    public CheckListResponseDto(Long userId, String userName, String event, LocalDate updatedDate) {
        this.user_id = userId;
        this.user_name = userName;
        this.event = event;
        this.updated_date = updatedDate;
    }

    public CheckListResponseDto(Schedule schedule) {
        this.user_id = schedule.getUser_id();
        this.user_name = schedule.getUser_name();
        this.event = schedule.getEvent();
        this.updated_date = schedule.getUpdate_date();
    }


}
