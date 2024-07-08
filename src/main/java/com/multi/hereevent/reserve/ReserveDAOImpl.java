package com.multi.hereevent.reserve;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.Map;
@Repository
@RequiredArgsConstructor
public class ReserveDAOImpl implements ReserveDAO{
    private final SqlSession sqlSession;
    @Override
    public int getReserveLimitByEventId(int event_no) {
        return sqlSession.selectOne("com.multi.hereevent.event.time.getReserveLimitByEventId", event_no);
    }

    @Override
    public int getReservedCountByEventId(int event_no, String reserve_date, String reserve_time) {
        return sqlSession.selectOne("com.multi.hereevent.event.time.getReservedCountByEventId", event_no);
    }

    @Override
    public void insertReservation(Map<String, Object> reservationInfo) {
        sqlSession.insert("com.multi.hereevent.event.time.insertReservation", reservationInfo);
    }
}
