package com.multi.hereevent.mail;

import com.multi.hereevent.dto.EventDTO;
import com.multi.hereevent.dto.MemberDTO;
import com.multi.hereevent.dto.WaitDTO;
import com.multi.hereevent.event.EventDAO;
import com.multi.hereevent.event.EventService;
import com.multi.hereevent.member.MemberService;
import com.multi.hereevent.wait.WaitService;
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
    public final WaitService waitService;

    /* 대기 3팀 남았을 경우 이메일 보내기 */

    /* 대기 등록 성공 시 이메일 보내기 */
    public void sendWaitSuccessEmail(WaitDTO wait) throws MessagingException {
        // 매개변수로 받아온 wait 에는 wait_no가 포함되어 있지 않으므로
        // 삽입된 대기의 wait_no 를 따로 가져오기
        WaitDTO curWait = waitService.getWaitInfo(wait.getWait_tel());
        // wait_no 로 이벤트 정보를 포함한 waitDTO 가져오기
        WaitDTO eventWait = waitService.eventDetail(curWait.getWait_no());

        // 이메일 전송
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

        messageHelper.setFrom("sy0223lee@gmail.com");
        messageHelper.setTo(wait.getEmail());
        messageHelper.setSubject("[HereEvent] '" + eventWait.getName() + "' 대기 등록 안내");
        messageHelper.setText(waitHtml(eventWait, curWait.getWait_no(), curWait.getEvent_no()), true);
        mailSender.send(message);
    }
    private String waitHtml(WaitDTO wait, int wait_no, int event_no){
        // 메일로 보낼 정보 조회
        int position = waitService.getWaitingPosition(event_no, wait_no); // 현재 내 순서
        int waitingCount = waitService.getWaitingCount(event_no); // 현재 총 대기 인원
        String waitTime = waitService.getEntranceWaitTime(event_no, wait_no); // 예상 대기 시간

        // 메일로 보낼 메시지 생성
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>");
        sb.append("<meta http-equiv='Content-Type' content='text/html; charset=euc-kr'>");
        sb.append("<h2>대기 중인 이벤트 안내</h2>")
            .append("<div>Date()에 작성된 내용이며 실시간 현황 확인은 <a href=\"http://localhost:9090/hereevent/main\">HereEvent</a>에서 해주시길 바랍니다.</div>")
            .append("    <img style='width:100px; height:100px; margin:10px 10px 10px 0;' src='http://localhost:9090/hereevent/download/event/wait.getImg_path()'/>")
            .append("    <p>이벤트 제목 <strong>wait.getName()</strong></p>")
            .append("    <p>이벤트 위치 <strong>wait.getAddr()</strong></p>")
            .append("    <p>대기 번호 <strong><span>wait.getWait_no()</span></strong></p>")
            .append("    <strong><p>waitTime</p></strong>")
            .append("    <p>" )
            .append("        <span style='display: inline;'>현재 내 순서 <strong>position번</strong></span>" )
            .append("        <span style='display: inline; margin-left: 10px;'> 현재 대기 인원 <strong>waitingCount팀</strong></span>")
            .append("    </p>")
            .append("    <button onclick='location.href=\"http://localhost:9090/hereevent/main\"'>HereEvent 대기 현황 확인 바로가기</button>");


        return sb.toString();
    }

    /* 추천 이메일 보내기 */
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
    /* 추천 이메일 내용 html 로 작성 */
    private String recommendHtml(List<EventDTO> eventList) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>");
        sb.append("<meta http-equiv='Content-Type' content='text/html; charset=euc-kr'>");
        sb.append("<h2>오픈 예정 이벤트 안내</h2>" +
                "<table><thead><tr>" +
                "<td>이미지</td><td>이벤트명</td><td>기간</td><td>위치</td>" +
                "</tr></thead><tbody>");
        for (EventDTO event : eventList) {
            sb.append("<tr><td><img style='width:100px; height:100px; margin:10px 10px 10px 0;' src='http://localhost:9090/hereevent/download/event/")
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