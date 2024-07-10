package com.multi.hereevent.reserve;

import com.multi.hereevent.dto.ReserveDTO;

import java.util.Map;

public interface ReserveService {
    String makeReservation(int event_no, int member_no, String reserve_date, String reserve_time);
    int deleteReservation(Map<String, Object> params);
    void updateReservation(Map<String, Object> params);
    ReserveDTO selectReserve(int event_no, int member_no, String reserve_date, String reserve_time);
}
