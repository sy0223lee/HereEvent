package com.multi.hereevent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Alias("memberImg")
public class MemberImgDTO {
    private int member_img_no;
    private int member_no;
    private String img_path;

    // 이미지 저장을 위한 생성자
    public MemberImgDTO(String img_path) {
            this.img_path = img_path;
        }

}
