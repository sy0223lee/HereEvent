package com.multi.hereevent.map;

import com.multi.hereevent.dto.EventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
@Service
@RequiredArgsConstructor
public class MapServiceImpl implements MapService{
    private final MapDAO dao;

    @Override
    public List<EventDTO> selectEventWithMap(String location, List<String> state, List<String> type) {
        return dao.selectEventWithMap(location, state, type);
    }

    @Override
    public String searchPath(double sx, double sy, double ex, double ey) throws IOException {
        return dao.fetchPathData(sx, sy, ex, ey);
    }

    @Override
    public String loadLane(String mapObj) throws IOException {
        return dao.fetchLaneData(mapObj);
    }}

