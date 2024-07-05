package com.multi.hereevent.wait;

import com.multi.hereevent.dto.WaitDTO;
import com.multi.hereevent.event.EventDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
public class WaitServiceImpl implements WaitService {
    private final WaitDAO dao;

    @Override
    public WaitDTO getWaitInfo(String wait_tel) {
        return dao.getWaitInfo(wait_tel);
    }

    @Override
    public int waitInsert(WaitDTO wait) {
        return dao.waitInsert(wait);
    }

    @Override
    public List<WaitDTO> getWaitList() {
        List<WaitDTO> waitlist = dao.getWaitList();
        return waitlist;
    }

    @Override
    public WaitDTO read(String wait_tel) {
        return null;
    }

    @Override
    public WaitDTO waitDetail(int wait_no) {
        return dao.waitDetail(wait_no);
    }

    @Override
    public WaitDTO eventDetail(int wait_no) {
        return dao.eventDetails(wait_no);
    }

    @Override
    public int waitDelete(int wait_no) {
        return dao.delete(wait_no);
    }

    // 본인의 대기 번호 조회
    @Override
    public int getWaitingPosition(int event_no, int wait_no) {
        List<WaitDTO> waitingList = dao.whenIgetInNo(event_no);
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
        List<WaitDTO> waitingList = dao.whenIgetInNo(event_no);
        return waitingList.size();
    }
    // 대기 상태를 cancel 또는 visit 로 수정
    @Override
    public int updateState(WaitDTO wait) {
        return dao.updateState(wait);
    }

    @Override
    public boolean canInsert(String wait_tel) {
        WaitDTO existingWait = dao.getWaitInfo(wait_tel);
        return existingWait == null;
    }
    @Override
    public String getEntranceWaitTime(int event_no, int wait_no) {
        List<WaitDTO> waitingList = dao.getWaitingListByEventNo(event_no);
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

        int waitGroups = position / 3;
        int waitMinutes = waitGroups * 3;

        return "예상 대기 시간 " + waitMinutes + "분";
    }

    @Override
    @Scheduled(fixedRate = 60000)  // 1분
    public void checkAndUpdateWaitStatus() {
        List<WaitDTO> waitingList = dao.getAllWaitingList();
        LocalDateTime now = LocalDateTime.now();

        for (WaitDTO wait : waitingList) {
            if ("visit".equals(wait.getState()) && Duration.between(wait.getWait_date(), now).toMinutes() >= 20) {
                wait.setState("cancel");
                dao.updateState(wait);
            }
        }
    }
}