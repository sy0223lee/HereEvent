package com.multi.hereevent.member;

import com.multi.hereevent.dto.CategoryInterestDTO;
import com.multi.hereevent.dto.MemberDTO;
import com.multi.hereevent.dto.MemberImgDTO;
import com.multi.hereevent.dto.ReviewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface MemberService {
    MemberDTO loginMember(MemberDTO member);
    int insertMember(MemberDTO member);
    List<MemberDTO> selectAllMember();
    MemberDTO selectMemberDetail(int member_no);
    int updateMemberNick(MemberDTO member);
    int updateMemberBirth(MemberDTO member);
    int updateMemberProfileImg(MemberDTO member);
    int deleteMember(int member_no);
    boolean checkMemberNick(String nick);
    boolean checkMemberEmail(String email);
    MemberDTO findMemberByEmail(String email);

    // 페이징 처리
    Page<MemberDTO> selectMemberWithPage(Map<String, Object> params, Pageable page);

}
