package com.multi.hereevent.reserve;

public interface ReserveService {
    String makeReservation(int event_no, int member_no, String reserve_date, String reserve_time);
}
