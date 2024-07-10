package com.multi.hereevent.map;

import com.multi.hereevent.dto.EventDTO;

import java.io.IOException;
import java.util.List;

public interface MapService {
    // 지도에서 가져온 값으로 이벤트 조회
    List<EventDTO> selectEventWithMap(String location, List<String> state, List<String> type);

    String getApiResponse() throws IOException;
}