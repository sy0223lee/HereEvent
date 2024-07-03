package com.multi.hereevent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Alias("event")
public class EventDTO {
    private int event_no; // 이벤트번호
    private int category_no; // 카테고리
    private String name; // 이벤트이름
    private Date start_date; //시작일자
    private Date end_date; //종료일자
    private String addr; //위치
    private String info; //팝업소개
    private String homepage; //브랜드홈페이지
    private String sns; // 브랜드 SNS링크
    private String img_path; //저장한 이미지 경로
    private String type; //사전, 대기, 전체
    private int reserve_limit; //시간당 제한인원(예약)
    private int wait_limit = 10; //시간당 제한인원(대기)
    private float avg_star;
    private MultipartFile event_img; // 이벤트 이미지 업로드를 위한 멤버변수

    // 관심 이벤트 설정 여부 저장을 위한 멤버변수
    private int event_interest_no;

    // 이벤트 크롤링 결과 insert 하기 위한 생성자
    public EventDTO(String name, Date start_date, Date end_date, String addr, String info) {
        this.name = name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.addr = addr;
        this.info = info;
    }
}
