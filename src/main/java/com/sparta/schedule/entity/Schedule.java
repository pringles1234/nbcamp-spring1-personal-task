package com.sparta.schedule.entity;

import com.sparta.schedule.dto.CreateScheduleRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor // 기본 생성자 자동 생성
public class Schedule {

    private Long schedule_id;
    private String user_name;
    private String password;
    private String event;
    private LocalDateTime created_dateTime;
    private LocalDateTime updated_dateTime;
    private LocalDate update_date;

    public void setSchdule_id(Long scheduleid){
        this.schedule_id = scheduleid;
    }

    public Long getSchedule_id(){
        return this.schedule_id;
    }

    public Schedule(CreateScheduleRequestDto createScheduleRequestDto) {
        this.user_name = createScheduleRequestDto.getUser_name();
        this.password = createScheduleRequestDto.getPassword();
        this.event = createScheduleRequestDto.getEvent();
    }


}
