package com.multi.hereevent.reserve;

import com.multi.hereevent.dto.ReserveDTO;

import java.util.List;
import java.util.Map;

public interface ReserveDAO {
    int getReserveLimitByEventId(int event_no);
    int getReservedCountByEventId(Map<String, Object> params);
    void insertReservation(Map<String, Object> reservationInfo);
    int cancelReservation(Map<String, Object> params);
    void updateReservation(Map<String, Object> params);
    int checkDuplicateReservation(int event_no, int member_no, String reserve_date, String reserve_time);
    ReserveDTO selectReserve(Map<String, Object> params);
    // 페이징 처리
    int countReserveWithPage(Map<String, Object> params);
    List<ReserveDTO> selectReserveWithPage(Map<String, Object> params);
    int cancelReserve(List<Integer> reserveNo); // 취소
}
