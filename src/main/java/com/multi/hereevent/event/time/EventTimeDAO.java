package com.multi.hereevent.event.time;

import com.multi.hereevent.dto.EventTimeDTO;

import java.util.List;
import java.util.Map;

public interface EventTimeDAO {
    int insertEventTimeList(List<EventTimeDTO> eventTimeList);
    int updateEventTimeList(List<EventTimeDTO> eventTimeList);
    EventTimeDTO getEventTimeByEventNoAndDay(int event_no,String day);
    List<EventTimeDTO> getEventTime(int event_no);
    List<String> getHolidayDays(int event_no);
}
