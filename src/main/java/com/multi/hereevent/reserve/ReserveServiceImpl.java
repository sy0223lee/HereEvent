package com.multi.hereevent.reserve;

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

        // 현재 예약된 인원 수 조회
        // 해당 날짜, 시간에 총 몇 명이 예약했는지 확인해야 하므로 date와 time 도 넘겨줘야함
        int currentReservationCount = dao.getReservedCountByEventId(event_no, reserve_date, reserve_time);

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
}
