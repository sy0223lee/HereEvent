package com.multi.hereevent.event.time;

import com.multi.hereevent.dto.EventTimeDTO;

import java.util.List;

public interface EventTimeDAO {
    int insertEventTimeList(List<EventTimeDTO> eventTimeList);
    EventTimeDTO getEventTimeByEventNoAndDay(int event_no,String day);
    List<EventTimeDTO> getEventTime(int event_no);
}
