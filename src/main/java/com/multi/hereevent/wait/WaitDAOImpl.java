package com.multi.hereevent.wait;

import com.multi.hereevent.dto.EventDTO;
import com.multi.hereevent.dto.MemberDTO;
import com.multi.hereevent.dto.WaitDTO;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


import java.util.List;
@Repository
@RequiredArgsConstructor
public class WaitDAOImpl implements WaitDAO {
    private final SqlSession sqlSession;

    @Override
    public WaitDTO waitLogin(WaitDTO wait) {
        return sqlSession.selectOne("com.multi.hereevent.wait.login", wait);
    }

    @Override
    public List<WaitDTO> getAllWaitingList() {
        return sqlSession.selectList("com.multi.hereevent.wait.getAllWaitingList");
    }
    @Override
    public WaitDTO findByWaitTelAndState(String wait_tel) {
//        Map<String, String> params = new HashMap<>();
//        params.put("wait_tel", wait_tel);
//        params.put("state", "wait");

        WaitDTO dto =  sqlSession.selectOne("com.multi.hereevent.wait.check", wait_tel);
        System.out.println("====================="+dto);
        return dto;
    }



    @Override
    public int waitInsert(WaitDTO wait) {
        return sqlSession.insert("com.multi.hereevent.wait.insert",wait);
    }

    @Override
    public int delete(int wait_no) {
        return sqlSession.delete("com.multi.hereevent.wait.delete", wait_no);
    }

    @Override
    public List<WaitDTO> getWaitList() {
        return sqlSession.selectList("com.multi.hereevent.wait.selectall");
    }

    @Override
    public WaitDTO read(String wait_no) {
        return sqlSession.selectOne("com.multi.hereevent.wait.read", wait_no);
    }

    @Override
    public WaitDTO waitDetail(int wait_no) {
        return sqlSession.selectOne("com.multi.hereevent.wait.detail",wait_no);
    }

    @Override
    public WaitDTO waitDetailTel(String wait_tel) {
        return sqlSession.selectOne("com.multi.hereevent.wait.detailTel",wait_tel);
    }

    @Override
    public WaitDTO eventDetails(int wait_no) {
        return sqlSession.selectOne("com.multi.hereevent.wait.EventDetails",wait_no);
    }

    @Override
    public int updateState(WaitDTO wait) {

        return sqlSession.update("com.multi.hereevent.wait.updateState", wait);
    }


    @Override
    public List<WaitDTO> whenIgetInNo(int event_no) {
        return sqlSession.selectList("com.multi.hereevent.wait.whenIgetInNo", event_no);
    }
    @Override
    public List<WaitDTO> getWaitingListByEventNo(int event_no) {
        return sqlSession.selectList("com.multi.hereevent.wait.getWaitingListByEventNo", event_no);
    }

}
