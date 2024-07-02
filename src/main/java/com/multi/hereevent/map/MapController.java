package com.multi.hereevent.map;

import com.multi.hereevent.dto.EventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MapController {
    private final MapService mapService;

    @GetMapping("/map/kakaomap.html")
    public String address(){
        System.out.println("카카오지도");
        return "kakaomap/kakaomap";
    }

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
}
