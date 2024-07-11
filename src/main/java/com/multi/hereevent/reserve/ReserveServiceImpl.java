package com.multi.hereevent.reserve;

import com.multi.hereevent.dto.ReserveDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReserveServiceImpl implements ReserveService{
    private final ReserveDAO dao;

    @Override
    public String makeReservation(int event_no, int member_no, String reserve_date, String reserve_time) {
        // 예약 가능 여부 확인
        int reserveLimit = dao.getReserveLimitByEventId(event_no);
        if (reserveLimit == 0) {
            return "예약을 할 수 없는 이벤트입니다.";
        }
        int count = dao.checkDuplicateReservation(event_no, member_no, reserve_date, reserve_time);
        if (count > 0) {
            return "이미 예약되었습니다.";
        }
        Map<String, Object> params = new HashMap<>();
        params.put("event_no", event_no);
        params.put("reserve_date", reserve_date);
        params.put("reserve_time", reserve_time);
        // 현재 예약된 인원 수 조회
        // 해당 날짜, 시간에 총 몇 명이 예약했는지 확인해야 하므로 date와 time 도 넘겨줘야함
        int currentReservationCount = dao.getReservedCountByEventId(params);

        if (currentReservationCount >= reserveLimit) {
            return "예약 인원이 초과되었습니다.";
        }
        // 예약 처리
        Map<String, Object> reservationInfo = new HashMap<>();
        reservationInfo.put("event_no", event_no);
        reservationInfo.put("member_no", member_no);
        reservationInfo.put("reserve_date", reserve_date);
        reservationInfo.put("reserve_time", reserve_time);


        dao.insertReservation(reservationInfo);
        return "예약되었습니다.";
    }

    @Override
    public int deleteReservation(Map<String, Object> params) {
        // String 타입의 날짜와 시간을 java.sql.Date와 java.sql.Time으로 변환
        String reserveDateStr = (String) params.get("reserve_date");
        String reserveTimeStr = (String) params.get("reserve_time");

        java.sql.Date reserveDate = java.sql.Date.valueOf(reserveDateStr);
        java.sql.Time reserveTime = java.sql.Time.valueOf(reserveTimeStr);

        params.put("reserve_date", reserveDate);
        params.put("reserve_time", reserveTime);
        return dao.deleteReservation(params);
    }

    @Override
    public void updateReservation(Map<String, Object> params) {
        String reserveDateStr = (String) params.get("reserve_date");
        String reserveTimeStr = (String) params.get("reserve_time");

        java.sql.Date reserveDate = java.sql.Date.valueOf(reserveDateStr);
        java.sql.Time reserveTime = java.sql.Time.valueOf(reserveTimeStr);

        params.put("reserve_date", reserveDate);
        params.put("reserve_time", reserveTime);
        dao.updateReservation(params);

    }

    @Override
    public ReserveDTO selectReserve(int event_no, int member_no, String reserve_date, String reserve_time) {
        Map<String, Object> params = new HashMap<>();
        params.put("event_no", event_no);
        params.put("member_no", member_no);
        params.put("reserve_date", reserve_date);
        params.put("reserve_time", reserve_time);
        return dao.selectReserve(params);
    }

}
