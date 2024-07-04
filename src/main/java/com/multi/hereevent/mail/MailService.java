package com.multi.hereevent.mail;

import com.multi.hereevent.dto.EventDTO;
import com.multi.hereevent.dto.MemberDTO;
import com.multi.hereevent.event.EventService;
import com.multi.hereevent.member.MemberService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender; // html 을 보내기 위한 mail sender
    public final MemberService memberService;
    public final EventService eventService;

    public void sendRecommendEmail() throws MessagingException {
        List<MemberDTO> memberList = memberService.selectAllMember(); // 모든 멤버 조회
        for (MemberDTO member : memberList) {
            List<EventDTO> eventList = eventService.selectNewEvent(member.getMember_no()); // 관심 카테고리 중 오픈 예정인 이벤트 조회

            if(!eventList.isEmpty()) { // 이벤트 리스트가 비어있지 않은 경우만 이메일 전송
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

                messageHelper.setFrom("sy0223lee@gmail.com");
                messageHelper.setTo(member.getEmail());
                messageHelper.setSubject("[HereEvent] " + member.getNick() + "님이 관심 있어 하실만한 오픈 예정 이벤트 안내");
                messageHelper.setText(recommendHtml(eventList), true);
                mailSender.send(message);
            }
        }
    }

    public String recommendHtml(List<EventDTO> eventList) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>");
        sb.append("<meta http-equiv='Content-Type' content='text/html; charset=euc-kr'>");
        sb.append("<h2>오픈 예정 이벤트 안내</h2>" +
                "<table><thead><tr>" +
                "<td>이미지</td><td>이벤트명</td><td>기간</td><td>위치</td>" +
                "</tr></thead><tbody>");
        for (EventDTO event : eventList) {
            sb.append("<tr><td'><img style='width:100px; height:100px; margin:10px 10px 10px 0;' src='http://localhost:9090/hereevent/download/event/")
                    .append(event.getImg_path()).append("'></td><td><a style='margin-right:10px;' href='http://localhost:9090/hereevent/event/")
                    .append(event.getEvent_no()).append("'>")
                    .append(event.getName()).append("</a></td><td><p style='margin-right:10px;'>")
                    .append(event.getStart_date()).append("~")
                    .append(event.getEnd_date()).append("</p></td><td><p>")
                    .append(event.getAddr()).append("</p></td></tr>");
        }
        sb.append("</tbody></table></body></html>");
        return sb.toString();
    }
}