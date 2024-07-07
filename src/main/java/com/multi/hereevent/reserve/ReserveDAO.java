package com.multi.hereevent.reserve;

import java.util.Map;

public interface ReserveDAO {
    int getReserveLimitByEventId(int event_no);
    int getReservedCountByEventId(int event_no, String reserve_date, String reserve_time);
    void insertReservation(Map<String, Object> reservationInfo);
}
