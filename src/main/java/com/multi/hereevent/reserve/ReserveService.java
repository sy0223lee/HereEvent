package com.multi.hereevent.reserve;

import com.multi.hereevent.dto.ReserveDTO;
import com.multi.hereevent.dto.ReviewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ReserveService {
    String makeReservation(int event_no, int member_no, String reserve_date, String reserve_time);
    int deleteReservation(Map<String, Object> params);
    void updateReservation(Map<String, Object> params);
    ReserveDTO selectReserve(int event_no, int member_no, String reserve_date, String reserve_time);
    // 페이징 처리
    Page<ReserveDTO> selectReserveWithPage(Map<String, Object> params, Pageable page);
    int cancelReserve(List<Integer> reserveNo);
}
