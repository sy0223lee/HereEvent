package com.multi.hereevent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Alias("wait")
public class WaitDTO {
    private int wait_no;
    private String wait_tel; // 대기자 전화번호
    private int event_no;
    private String email;
    private LocalDateTime wait_date;
    private String state;
    private String send_mail;

    // 이벤트 정보
    private String name; // 이벤트이름
    private String addr; //위치
    private String img_path;
}
