package com.multi.hereevent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.sql.Date;
import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Alias("memberEvent")
public class MemberEventDTO {
    private int index_no; // 예약, 대기 번호
    private int event_no; // 이벤트번호
    private String name; // 이벤트이름
    private String addr; // 위치
    private String img_path; // 저장한 이미지 경로
    private String state; // reserve, wait, visit, cancel
    private Date enter_date; // 입장 날짜, 예약인 경우 reserve_date, 대기인 경우 wait_date
    private Time enter_time; // 입장 시간, 예약인 경우 reserve_time, 대기인 경우 null
}
