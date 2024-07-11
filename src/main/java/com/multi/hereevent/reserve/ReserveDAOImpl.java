package com.multi.hereevent.reserve;

import com.multi.hereevent.dto.ReserveDTO;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
@RequiredArgsConstructor
public class ReserveDAOImpl implements ReserveDAO{
    private final SqlSession sqlSession;
    @Override
    public int getReserveLimitByEventId(int event_no) {
        return sqlSession.selectOne("com.multi.hereevent.reserve.getReserveLimitByEventId", event_no);
    }

    @Override
    public int getReservedCountByEventId(Map<String, Object> params) {
        return sqlSession.selectOne("com.multi.hereevent.reserve.getReservedCountByEventId", params);
    }

    @Override
    public void insertReservation(Map<String, Object> reservationInfo) {
        sqlSession.insert("com.multi.hereevent.reserve.insertReservation", reservationInfo);
    }

    @Override
    public int deleteReservation(Map<String, Object> params) {
        return sqlSession.delete("com.multi.hereevent.reserve.deleteReservation", params);
    }

    @Override
    public void updateReservation(Map<String, Object> params) {
        sqlSession.update("com.multi.hereevent.reserve.updateReservation", params);
    }

    @Override
    public int checkDuplicateReservation(int event_no, int member_no, String reserve_date, String reserve_time) {
        Map<String, Object> params = new HashMap<>();
        params.put("event_no", event_no);
        params.put("member_no", member_no);
        params.put("reserve_date", reserve_date);
        params.put("reserve_time", reserve_time);
        return sqlSession.selectOne("com.multi.hereevent.reserve.checkDuplicateReservation", params);
    }
  
    @Override
    public ReserveDTO selectReserve(Map<String, Object> params) {
        return sqlSession.selectOne("com.multi.hereevent.reserve.selectReserve", params);
    }

    @Override
    public int countReserveWithPage(Map<String, Object> params) {
        return sqlSession.selectOne("com.multi.hereevent.reserve.countReserveWithPage", params);
    }

    @Override
    public List<ReserveDTO> selectReserveWithPage(Map<String, Object> params) {
        return sqlSession.selectList("com.multi.hereevent.reserve.selectReserveWithPage", params);
    }

    @Override
    public int cancelReserve(List<Integer> reserveNo) {
        return sqlSession.update("com.multi.hereevent.reserve.cancelReserve", reserveNo);
    }
}
