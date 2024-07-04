package com.multi.hereevent.event.time;

import com.multi.hereevent.dto.EventTimeDTO;

import java.util.List;

public interface EventTimeService {
    int insertEventTimeList(List<EventTimeDTO> eventTimeList);
    List<String> getOperTime(int event_no, String day);
    EventTimeDTO getEventTimeByEventNoAndDay(int event_no, String day);
    List<String> getHolidayDays(int event_no);

}
