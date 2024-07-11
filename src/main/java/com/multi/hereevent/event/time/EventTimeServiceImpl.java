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
    public int updateEventTImeList(List<EventTimeDTO> eventTimeList) {
        return dao.updateEventTimeList(eventTimeList);
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

        List<String> timeList = new ArrayList<>();
        int openTimeInt = Integer.parseInt(openTime[0]);
        int closeTimeInt = Integer.parseInt(closeTime[0]);
        for(int i=openTimeInt;i<=closeTimeInt;i++){
            timeList.add(String.valueOf(i)+":"+openTime[1]+":"+openTime[2]);
        }
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


}
