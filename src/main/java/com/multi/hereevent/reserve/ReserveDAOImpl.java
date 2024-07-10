package com.multi.hereevent.reserve;

import com.multi.hereevent.dto.ReserveDTO;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
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
}
