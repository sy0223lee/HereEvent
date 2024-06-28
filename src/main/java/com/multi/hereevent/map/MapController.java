package com.multi.hereevent.map;

import com.multi.hereevent.dto.ButtonDTO;
import com.multi.hereevent.dto.EventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MapController {
    private final MapService mapService;

    @GetMapping("/map/kakaomap.html")
    public String address(){
        System.out.println("카카오지도");
        return "kakaomap/kakaomap";
    }

    @GetMapping("/map/clicktest")
    public String clicktest(){
        return "kakaomap/clicktest";
    }

    @PostMapping("/map/clicktest/ajaxtest")
    @ResponseBody
    public List<EventDTO> ajaxtest(ButtonDTO buttonDTO){
        List<EventDTO> list = mapService.button(buttonDTO);
        System.out.println(buttonDTO);
        System.out.println(buttonDTO.getType());
        System.out.println(buttonDTO.getState());
        return list;
    }
}
