package com.multi.hereevent.event.time;

import com.multi.hereevent.dto.EventTimeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventTimeServiceImpl implements EventTimeService{
    private final EventTimeDAO dao;

    @Override
    public int insertEventTimeList(List<EventTimeDTO> eventTimeList) {
        return dao.insertEventTimeList(eventTimeList);
    }

    @Override
    public List<String> getOperTime(int event_no, String day) {
        EventTimeDTO eventTime = dao.getEventTimeByEventNoAndDay(event_no,day);

        // eventTime이 null인 경우 예외 처리
        if (eventTime == null) {
            throw new IllegalArgumentException("해당 이벤트 번호와 요일에 대한 운영 시간이 없습니다.");
        }
        String[] openTime = eventTime.getOpen_time().split(":");
        String[] closeTime = eventTime.getClose_time().split(":");

        System.out.println(openTime[0]);
        List<String> timeList = new ArrayList<>();
        int openTimeInt = Integer.parseInt(openTime[0]);
        int closeTimeInt = Integer.parseInt(closeTime[0]);
        for(int i=openTimeInt;i<=closeTimeInt;i++){
            timeList.add(String.valueOf(i)+":"+openTime[1]+":"+openTime[2]);
        }
        System.out.println("timeList===>"+timeList);
        return timeList;
    }

    @Override
    public EventTimeDTO getEventTimeByEventNoAndDay(int event_no, String day){
        return dao.getEventTimeByEventNoAndDay(event_no,day);
    }

    @Override
    public List<EventTimeDTO> getEventTime(int event_no){
        return dao.getEventTime(event_no);
    }

    @Override
    public List<String> getHolidayDays(int event_no) {
        return dao.getHolidayDays(event_no);
    }

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
