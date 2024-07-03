package com.multi.hereevent.member;

import com.multi.hereevent.dto.CategoryInterestDTO;
import com.multi.hereevent.dto.MemberDTO;
import com.multi.hereevent.dto.MemberImgDTO;
import com.multi.hereevent.dto.ReviewDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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
        return dao.deleteMember(member_no);
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

    @Override
    public Page<MemberDTO> selectMemberWithPage(Map<String, Object> params, Pageable page) {
        int count = dao.countMemberWithPage(params);
        params.put("offset", page.getOffset());
        params.put("pageSize", page.getPageSize());
        List<MemberDTO> memberList = dao.selectMemberWithPage(params);
        return new PageImpl<>(memberList, page, count);
    }

    @Override
    public int RegMember(MemberDTO member, MemberImgDTO memberImgDTO) {
        if(memberImgDTO == null){
            return dao.RegMember(member);
        }else{
            dao.RegMember(member);
            return dao.insertMemberImg(memberImgDTO);
        }
    }

}
