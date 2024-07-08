package com.multi.hereevent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Alias("chart")
public class ChartDTO {
    private Date date; // 날짜
    private int start_cnt; // 시작 이벤트 카운트
    private int end_cnt; // 종료 이벤트 카운트
    private int cnt; // 카테고리별 이벤트 카운트 or 신규 회원 카운트 or 예약/대기 카운트
    private String name; // 카테고리 이름 or 이벤트 이름
}
