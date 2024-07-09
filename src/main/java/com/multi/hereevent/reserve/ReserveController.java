package com.multi.hereevent.reserve;

import com.multi.hereevent.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
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
    }
    @GetMapping("/reservation/delete")
    public String deleteReservation(@RequestParam("event_no") int event_no,
                                    @RequestParam("reserve_date") String reserve_date,
                                    @RequestParam("reserve_time") String reserve_time,
                                    Model model) {
        /*
        System.out.println("event_no: " + event_no);
        System.out.println("reserve_date: " + reserve_date);
        System.out.println("reserve_time: " + reserve_time);*/

        MemberDTO member = (MemberDTO) model.getAttribute("member");
        if (member != null) {
            Map<String, Object> params = new HashMap<>();
            int member_no = member.getMember_no();
            params.put("event_no", event_no);
            params.put("member_no", member_no);
            params.put("reserve_date", reserve_date);
            params.put("reserve_time", reserve_time);

            // 예약 삭제 서비스 호출
            reserveService.deleteReservation(params);
        } else {
            // 회원 정보가 세션에 없는 경우, 예외 처리
            throw new IllegalStateException("Member not found in session");
        }
        return "redirect:/myevent";
    }

    @GetMapping("/reservation/update")
    public String updateReservation(@RequestParam("event_no") int event_no,
                                    @RequestParam("reserve_date") String reserve_date,
                                    @RequestParam("reserve_time") String reserve_time,
                                    Model model) {
        MemberDTO member = (MemberDTO) model.getAttribute("member");
        if (member != null) {
            Map<String, Object> params = new HashMap<>();
            int member_no = member.getMember_no();
            params.put("event_no", event_no);
            params.put("member_no", member_no);
            params.put("reserve_date", reserve_date);
            params.put("reserve_time", reserve_time);

            // 예약 삭제 서비스 호출
            reserveService.updateReservation(params);
        } else {
            // 회원 정보가 세션에 없는 경우, 예외 처리
            throw new IllegalStateException("Member not found in session");
        }
        return "redirect:/myevent";
    }
}
