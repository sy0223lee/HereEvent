package com.multi.hereevent.wait;

import com.multi.hereevent.dto.WaitDTO;
import com.multi.hereevent.event.EventDAO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WaitServiceImpl implements WaitService {
    private final WaitDAO waitDAO;
    private final EventDAO eventDAO;
    private final JavaMailSender mailSender; // html 을 보내기 위한 mail sender

    @Override
    public WaitDTO getWaitInfo(String wait_tel) {
        return waitDAO.getWaitInfo(wait_tel);
    }

    @Override
    public int waitInsert(WaitDTO wait) {
        return waitDAO.waitInsert(wait);
    }

    @Override
    public List<WaitDTO> getWaitList() {
        return waitDAO.getWaitList();
    }

    @Override
    public WaitDTO read(String wait_tel) {
        return null;
    }

    @Override
    public WaitDTO waitDetail(int wait_no) {
        return waitDAO.waitDetail(wait_no);
    }

    @Override
    public WaitDTO eventDetail(int wait_no) {
        return waitDAO.eventDetails(wait_no);
    }

    @Override
    public int waitDelete(int wait_no) {
        return waitDAO.delete(wait_no);
    }

    // 본인의 대기 번호 조회
    @Override
    public int getWaitingPosition(int event_no, int wait_no) {
        List<WaitDTO> waitingList = waitDAO.whenIgetInNo(event_no);
        for (int i = 0; i < waitingList.size(); i++) {
            if (waitingList.get(i).getWait_no() == wait_no) {
                return i + 1;
            }
        }
        return -1;
    }
    // 해당 이벤트에 대기 중인 전체 인원 조회
    @Override
    public int getWaitingCount(int event_no) {
        List<WaitDTO> waitingList = waitDAO.whenIgetInNo(event_no);
        return waitingList.size();
    }
    // 대기 상태를 cancel 또는 visit 로 수정
    @Override
    public int updateState(WaitDTO wait) {
        return waitDAO.updateState(wait);
    }

    @Override
    public boolean canInsert(String wait_tel) {
        WaitDTO existingWait = waitDAO.getWaitInfo(wait_tel);
        return existingWait == null;
    }
    @Override
    public String getEntranceWaitTime(int event_no, int wait_no) {
        List<WaitDTO> waitingList = waitDAO.getWaitingListByEventNo(event_no);
        waitingList.removeIf(wait -> !"wait".equals(wait.getState()));
        waitingList.sort((w1, w2) -> Integer.compare(w1.getWait_no(), w2.getWait_no()));

        int position = -1;
        for (int i = 0; i < waitingList.size(); i++) {
            if (waitingList.get(i).getWait_no() == wait_no) {
                position = i;
                break;
            }
        }

        if (position == -1) {
            throw new IllegalArgumentException("Invalid wait_no: " + wait_no);
        }
        // 3팀당 3분 대기
        int waitGroups = position / 3;
        int waitMinutes = waitGroups * 3;

        return "예상 대기 시간 " + waitMinutes + "분";
    }

    @Override
    @Scheduled(fixedRate = 60000)  // 1분
    public void checkAndUpdateWaitStatus() {
        log.info("대기 스케줄러 실행");
        LocalDateTime now = LocalDateTime.now();

        List<Integer> eventList = eventDAO.selectWaitEvent(); // 진행 중이고 현장대기 가능한 이벤트 조회

        for(Integer eventNo : eventList) {
            List<WaitDTO> updateWaitList = waitDAO.selectWaitToUpdate(eventNo); // wait_date 수정할 대기와 메일을 보내야하는 대기 조회
            if(!updateWaitList.isEmpty()) { // 대기열이 비어있지 않은 경우
                WaitDTO stateWait = updateWaitList.get(0); // 입장해야 하는 wait

                // state 가 wait 인 경우
                if (stateWait.getState().equals("wait")) {
                    log.info("대기 상태 able 변경");
                    // wait_date 를 현재 시간으로 수정
                    stateWait.setWait_date(now);
                    // state 를 able 로 수정
                    stateWait.setState("able");
                    waitDAO.updateState(stateWait);
                }
                // state 가 able 이고 wait_date 가 현재 시간 보다 20분 이전인 경우
                else if (stateWait.getState().equals("able") && Duration.between(stateWait.getWait_date(), now).toMinutes() >= 20) {
                    log.info("대기 상태 cancel 변경");
                    // state 를 cancel 로 수정
                    stateWait.setState("cancel");
                    waitDAO.updateState(stateWait);
                }

                if(updateWaitList.size() == 2) { // 메일 보낼 대기가 존재하는지 확인
                    WaitDTO sendWait = updateWaitList.get(1); // 메일 전송해야 하는 wait

                    // 자신의 앞에 3팀이 남은 경우 send_mail 컬럼이 null 이면 이메일 전송 후 send 으로 변경
                    if (sendWait.getSend_mail() == null) {
                        log.info("입장 안내 메일 전송");
                        // 이메일 전송하기
                        sendWaitAlmostEmail(sendWait);
                        // send_mail 을 send 로 변경
                        sendWait.setSend_mail("send");
                        waitDAO.updateState(sendWait);
                    }
                }
            }
        }
    }

    /* 대기 3팀 남았을 경우 이메일 보내기 */
    // MailService.java 에 구현 시 The dependencies of some of the beans in the application context form a cycle 오류 발생하므로 WaitServiceImpl 에서 구현...
    private void sendWaitAlmostEmail(WaitDTO wait) {
        // wait_no 로 이벤트 정보를 포함한 waitDTO 가져오기
        WaitDTO eventWait = eventDetail(wait.getWait_no());
        try {
            // 이메일 전송
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

            messageHelper.setFrom("sy0223lee@gmail.com");
            messageHelper.setTo(wait.getEmail());
            messageHelper.setSubject("[HereEvent] '" + eventWait.getName() + "' 대기 입장 안내");
            messageHelper.setText(waitAlmostHtml(eventWait, wait.getWait_no(), wait.getEvent_no()), true);
            mailSender.send(message);
        } catch (MessagingException e){
            e.printStackTrace();
        }
    }
    private String waitAlmostHtml(WaitDTO wait, int wait_no, int event_no){
        // 메일로 보낼 정보 조회
        int position = getWaitingPosition(event_no, wait_no); // 현재 내 순서
        int waitingCount = getWaitingCount(event_no); // 현재 총 대기 인원
        String waitTime = getEntranceWaitTime(event_no, wait_no); // 예상 대기 시간

        // 메일로 보낼 메시지 생성
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        sb.append("<html><body>")
                .append("<meta http-equiv='Content-Type' content='text/html; charset=euc-kr'>")
                .append("<h2>곧 입장이 가능합니다. 가까운 곳에 와서 대기해 주시기 바랍니다.</h2>")
                .append("<h3 style='color: #E14533'>입장 가능 상태가 된 후 20분 내로 입장하지 않으시면 취소되니 주의 바랍니다.</h3>")
                .append("<p style='margin: 10px;'><strong>").append(sdf.format(new Date())).append("에 작성된 내용이며 실시간 현황 확인은 <a href=href='http://localhost:9090/hereevent/wait/login'>HereEvent</a>에서 해주시길 바랍니다.</strong></p>")
                .append("<img style='margin: 10px; width:200px; height:200px; margin:10px 10px 10px 0;' src='http://localhost:9090/hereevent/download/event/").append(wait.getImg_path()).append("'/>")
                .append("<p style='margin: 10px;'>이벤트명 : <strong>").append(wait.getName()).append("</strong></p>")
                .append("<p style='margin: 10px;'>주소 : <strong>").append(wait.getAddr()).append("</strong></p>")
                .append("<p style='margin: 10px;'>대기번호 : <strong>").append(wait.getWait_no()).append("</strong></p>")
                .append("<h3 style='margin: 10px; color: #E14533'>").append(waitTime).append("</h3>")
                .append("<h3 style='margin: 10px; color: #E14533'>내 순서 : ").append(position).append("번</h3>")
                .append("<p style='margin: 10px;'>총 대기 인원 : <strong>").append(waitingCount).append("팀</strong></p>")
                .append("</body></html>");
        return sb.toString();
    }
}