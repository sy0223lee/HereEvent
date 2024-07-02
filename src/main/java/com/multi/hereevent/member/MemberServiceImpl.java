package com.multi.hereevent.member;

import com.multi.hereevent.dto.CategoryInterestDTO;
import com.multi.hereevent.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberDAO dao;

    @Override
    public MemberDTO loginMember(MemberDTO member) {
        return dao.loginMember(member);
    }

    @Override
    public int insertMember(MemberDTO member) {
        return dao.insertMember(member);
    }

    @Override
    public List<MemberDTO> selectAllMember() {
        return List.of();
    }

    @Override
    public MemberDTO selectMemberDetail(int member_no) {
        return dao.selectMemberDetail(member_no);
    }

    @Override
    public int updateMemberNick(MemberDTO member) {
        return dao.updateMemberNick(member);
    }

    @Override
    public int updateMemberBirth(MemberDTO member) {
        return dao.updateMemberBirth(member);
    }

    @Override
    public int updateMemberProfileImg(MemberDTO member) {
        return dao.updateMemberProfileImg(member);
    }

    @Override
    public int deleteMember(int member_no) {
        return 0;
    }

    @Override
    public boolean checkMemberNick(String nick) {
        return dao.checkMemberNick(nick);
    }

    @Override
    public boolean checkMemberEmail(String email) {
        return dao.checkMemberEmail(email);
    }

    @Override
    public MemberDTO findMemberByEmail(String email) {
        return dao.findMemberByEmail(email);
    }

}
