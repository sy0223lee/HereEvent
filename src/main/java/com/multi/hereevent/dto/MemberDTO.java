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
@Alias("member")
public class MemberDTO {
    private int member_no; // 회원번호
    private String email; // 이메일
    private String pass; // 비밀번호
    private String name; // 이름
    private String nick; // 닉네임
    private String tel; // 전화번호
    private Date birth; // 생일
    private Date reg_date; // 등록 날짜
    private String img_path; // 프로필 사진 경로
    private int mgr; // 관리자 여부, 0 = member, 1 = manager
    private MultipartFile profile_img; // 프로필 이미지 업로드를 위한 멤버변수

    // 네이버 로그인을 위한 생성자
    public MemberDTO(String email, String pass, String name, String nick, String tel){
        this.email = email;
        this.pass = pass;
        this.name = name;
        this.nick = nick;
        this.tel = tel;
        this.mgr = 0;
    }


}
