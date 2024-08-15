package com.sparta.schedule.dto;

import lombok.Getter;

@Getter
public class CreateScheduleRequestDto {

    private String user_name;
    private String password;
    private String event;
}
