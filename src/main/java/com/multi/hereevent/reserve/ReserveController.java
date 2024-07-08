package com.multi.hereevent.reserve;

import com.multi.hereevent.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@SessionAttributes("member")
public class ReserveController {
    private final ReserveService reserveService;
    // 예약기능
    @PostMapping("/event/reservation")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public String reservation(@RequestBody Map<String, String> data, Model model) {
        MemberDTO member = (MemberDTO) model.getAttribute("member");
        if (member == null) {
            JSONObject json = new JSONObject();
            json.put("message", "로그인을 해주세요.");
            return json.toJSONString();
        }
        System.out.println("Request Data: " + data);

        int event_no = Integer.parseInt(data.get("eventNo"));
        int member_no = member.getMember_no();
        data.put("memberNo", String.valueOf(member_no));
        String reserve_date = data.get("reserveDate");
        String reserve_time = data.get("reserveTime");

//        System.out.println("[reserve] " + event_no + ", " + date + ", " + time);
        model.addAttribute("memberNo", member_no);
        model.addAttribute("eventNo", event_no);
        model.addAttribute("reserveDate", reserve_date);
        model.addAttribute("reserveTime", reserve_time);
        String message = reserveService.makeReservation(event_no, member_no, reserve_date, reserve_time);
        JSONObject json = new JSONObject();
        json.put("message", message);

        return json.toJSONString();
    }}
