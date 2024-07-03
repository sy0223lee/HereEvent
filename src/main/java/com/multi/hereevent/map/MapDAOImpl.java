package com.multi.hereevent.map;

import com.multi.hereevent.dto.EventDTO;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class MapDAOImpl implements MapDAO{
    private final SqlSession sqlSession;

    @Override
    public List<EventDTO> selectEventWithMap(String location, List<String> state, List<String> type) {
        Map<String, Object> params = new HashMap<>();
        params.put("location", location);
        params.put("state", state);
        params.put("type", type);
        return sqlSession.selectList("com.multi.hereevent.map.selectEventWithMap", params);
    }
}
