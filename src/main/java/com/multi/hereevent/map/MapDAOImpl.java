package com.multi.hereevent.map;

import com.multi.hereevent.dto.EventDTO;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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
    @Override
    public String fetchData() throws IOException {
        String apiKey = "EAjsfQjX+4g3aWV+jGpR3ITd/hhxlaqly0wDShrjoFE";
        String urlInfo = "https://api.odsay.com/v1/api/searchPubTransPathT?SX=126.9027279&SY=37.5349277&EX=126.9145430&EY=37.5499421&apiKey=" + URLEncoder.encode(apiKey, "UTF-8");

        URL url = new URL(urlInfo);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }
        bufferedReader.close();
        conn.disconnect();

        return sb.toString();
    }

}
