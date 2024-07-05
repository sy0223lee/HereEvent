package com.multi.hereevent.mail;

import com.multi.hereevent.dto.EventDTO;
import com.multi.hereevent.dto.MemberDTO;
import com.multi.hereevent.dto.WaitDTO;
import com.multi.hereevent.event.EventService;
import com.multi.hereevent.member.MemberService;
import com.multi.hereevent.wait.WaitService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender; // html 을 보내기 위한 mail sender
    public final MemberService memberService;
    public final EventService eventService;
    public final WaitService waitService;

    /* 대기 3팀 남았을 경우 이메일 보내기 */
    public void sendWaitAlmostEmail(WaitDTO wait) {
        // 매개변수로 받아온 wait 에는 wait_no가 포함되어 있지 않으므로
        // 삽입된 대기의 wait_no 를 따로 가져오기
        WaitDTO curWait = waitService.getWaitInfo(wait.getWait_tel());
        // wait_no 로 이벤트 정보를 포함한 waitDTO 가져오기
        WaitDTO eventWait = waitService.eventDetail(curWait.getWait_no());

        try {
            // 이메일 전송
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

            messageHelper.setFrom("sy0223lee@gmail.com");
            messageHelper.setTo(wait.getEmail());
            messageHelper.setSubject("[HereEvent] '" + eventWait.getName() + "' 대기 등록 안내");
            messageHelper.setText(waitAlmostHtml(eventWait, curWait.getWait_no(), curWait.getEvent_no()), true);
            mailSender.send(message);
        } catch (MessagingException e){
            e.printStackTrace();
        }
    }
    private String waitAlmostHtml(WaitDTO wait, int wait_no, int event_no){
        // 메일로 보낼 정보 조회
        int position = waitService.getWaitingPosition(event_no, wait_no); // 현재 내 순서
        int waitingCount = waitService.getWaitingCount(event_no); // 현재 총 대기 인원
        String waitTime = waitService.getEntranceWaitTime(event_no, wait_no); // 예상 대기 시간

        // 메일로 보낼 메시지 생성
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        sb.append("<html><body>")
                .append("<meta http-equiv='Content-Type' content='text/html; charset=euc-kr'>")
                .append("<h2>새로운 이벤트 대기가 등록되었습니다.</h2>")
                .append("<p style='margin: 10px;'><strong>").append(sdf.format(new Date())).append("에 작성된 내용이며 실시간 현황 확인은 <a href=href='http://localhost:9090/hereevent/wait/login'>HereEvent</a>에서 해주시길 바랍니다.</strong></p>")
                .append("<img style='margin: 10px; width:200px; height:200px; margin:10px 10px 10px 0;' src='http://localhost:9090/hereevent/download/event/").append(wait.getImg_path()).append("'/>")
                .append("<p style='margin: 10px;'>이벤트명 : <strong>").append(wait.getName()).append("</strong></p>")
                .append("<p style='margin: 10px;'>주소 : <strong>").append(wait.getAddr()).append("</strong></p>")
                .append("<p style='margin: 10px;'>대기번호 : <strong>").append(wait.getWait_no()).append("</strong></p>")
                .append("<h3 style='margin: 10px; color: #E14533'>").append(waitTime).append("</h3>")
                .append("<h3 style='margin: 10px; color: #E14533'>내 순서 : ").append(position).append("번</h3?")
                .append("<p style='margin: 10px;'>대기 인원 : <strong>").append(waitingCount).append("팀</strong></p>")
                .append("</body></html>");
        return sb.toString();
    }

    /* 대기 등록 성공 시 이메일 보내기 */
    public void sendWaitSuccessEmail(WaitDTO wait) {
        // 매개변수로 받아온 wait 에는 wait_no가 포함되어 있지 않으므로
        // 삽입된 대기의 wait_no 를 따로 가져오기
        WaitDTO curWait = waitService.getWaitInfo(wait.getWait_tel());
        // wait_no 로 이벤트 정보를 포함한 waitDTO 가져오기
        WaitDTO eventWait = waitService.eventDetail(curWait.getWait_no());

        try {
            // 이메일 전송
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

            messageHelper.setFrom("sy0223lee@gmail.com");
            messageHelper.setTo(wait.getEmail());
            messageHelper.setSubject("[HereEvent] '" + eventWait.getName() + "' 대기 등록 안내");
            messageHelper.setText(waitSuccessHtml(eventWait, curWait.getWait_no(), curWait.getEvent_no()), true);
            mailSender.send(message);
        } catch (MessagingException e){
            e.printStackTrace();
        }
    }
    private String waitSuccessHtml(WaitDTO wait, int wait_no, int event_no){
        // 메일로 보낼 정보 조회
        int position = waitService.getWaitingPosition(event_no, wait_no); // 현재 내 순서
        int waitingCount = waitService.getWaitingCount(event_no); // 현재 총 대기 인원
        String waitTime = waitService.getEntranceWaitTime(event_no, wait_no); // 예상 대기 시간

        // 메일로 보낼 메시지 생성
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        sb.append("<html><body>")
            .append("<meta http-equiv='Content-Type' content='text/html; charset=euc-kr'>")
            .append("<h2>새로운 이벤트 대기가 등록되었습니다.</h2>")
            .append("<p style='margin: 10px;'><strong>").append(sdf.format(new Date())).append("에 작성된 내용이며 실시간 현황 확인은 <a href=href='http://localhost:9090/hereevent/wait/login'>HereEvent</a>에서 해주시길 바랍니다.</strong></p>")
            .append("<img style='margin: 10px; width:200px; height:200px; margin:10px 10px 10px 0;' src='http://localhost:9090/hereevent/download/event/").append(wait.getImg_path()).append("'/>")
            .append("<p style='margin: 10px;'>이벤트명 : <strong>").append(wait.getName()).append("</strong></p>")
            .append("<p style='margin: 10px;'>주소 : <strong>").append(wait.getAddr()).append("</strong></p>")
            .append("<p style='margin: 10px;'>대기번호 : <strong>").append(wait.getWait_no()).append("</strong></p>")
            .append("<h3 style='margin: 10px; color: #E14533'>").append(waitTime).append("</h3>")
            .append("<h3 style='margin: 10px; color: #E14533'>내 순서 : ").append(position).append("번</h3?")
            .append("<p style='margin: 10px;'>대기 인원 : <strong>").append(waitingCount).append("팀</strong></p>")
            .append("</body></html>");
        return sb.toString();
    }

    /* 추천 이메일 보내기 */
    @Scheduled(cron = "0 0 9 ? * 1") // 매주 월요일 9시마다 실행
    public void sendRecommendEmail() {
        List<MemberDTO> memberList = memberService.selectAllMember(); // 모든 멤버 조회
        for (MemberDTO member : memberList) {
            List<EventDTO> eventList = eventService.selectNewEvent(member.getMember_no()); // 관심 카테고리 중 오픈 예정인 이벤트 조회

            if(!eventList.isEmpty()) { // 이벤트 리스트가 비어있지 않은 경우만 이메일 전송
                try {
                    MimeMessage message = mailSender.createMimeMessage();
                    MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

                    messageHelper.setFrom("sy0223lee@gmail.com");
                    messageHelper.setTo(member.getEmail());
                    messageHelper.setSubject("[HereEvent] " + member.getNick() + "님이 관심 있어 하실만한 오픈 예정 이벤트 안내");
                    messageHelper.setText(recommendHtml(eventList), true);
                    mailSender.send(message);
                } catch (MessagingException e){
                    e.printStackTrace();
                }
            }
        }
    }
    /* 추천 이메일 내용 html 로 작성 */
    private String recommendHtml(List<EventDTO> eventList) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>")
            .append("<meta http-equiv='Content-Type' content='text/html; charset=euc-kr'>")
            .append("<h2>오픈 예정 이벤트 안내</h2>" +
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