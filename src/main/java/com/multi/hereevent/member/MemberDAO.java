package com.multi.hereevent.member;

import com.multi.hereevent.dto.*;

import java.util.List;
import java.util.Map;

public interface MemberDAO {
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
    int countMemberWithPage(Map<String, Object> params);
    List<MemberDTO> selectMemberWithPage(Map<String, Object> params);

}
