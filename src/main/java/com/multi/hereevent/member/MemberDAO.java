package com.multi.hereevent.member;

import com.multi.hereevent.dto.CategoryInterestDTO;
import com.multi.hereevent.dto.MemberDTO;

import java.util.List;

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
}
