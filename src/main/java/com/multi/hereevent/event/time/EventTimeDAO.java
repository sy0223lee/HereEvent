package com.multi.hereevent.event.time;

import com.multi.hereevent.dto.EventTimeDTO;

import java.util.List;
import java.util.Map;

public interface EventTimeDAO {
    int insertEventTimeList(List<EventTimeDTO> eventTimeList);
    EventTimeDTO getEventTimeByEventNoAndDay(int event_no,String day);
    List<String> getHolidayDays(int event_no);
}
