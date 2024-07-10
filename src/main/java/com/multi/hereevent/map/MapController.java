package com.multi.hereevent.map;

import com.multi.hereevent.dto.EventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MapController {
    private final MapService mapService;

    @GetMapping("/map")
    public String mapPage(){
        return "kakaomap/map";
    }

    // 지도 조건에 맞는 이벤트 조회해서 JSON 으로 넘기기
    @PostMapping("/map/list")
    @ResponseBody
    public List<EventDTO> selectEventWithMap(@RequestBody Map<String, Object> data){
        String location = (String) data.get("location");
        ArrayList<String> state =  (ArrayList<String>) data.get("state");
        ArrayList<String> type = (ArrayList<String>) data.get("type");

        return mapService.selectEventWithMap(location, state, type);
    }
    @GetMapping("/route")
    public String route(){
        return "kakaomap/findrouteEX";
    }

    @GetMapping("/searchpath")
    @ResponseBody
    public String searchPath(@RequestParam double sx, @RequestParam double sy, @RequestParam double ex, @RequestParam double ey) throws IOException {
        return mapService.searchPath(sx, sy, ex, ey);
    }

    @GetMapping("/loadLane")
    @ResponseBody
    public String loadLane(@RequestParam String mapObj) throws IOException {
        return mapService.loadLane(mapObj);
    }

}
