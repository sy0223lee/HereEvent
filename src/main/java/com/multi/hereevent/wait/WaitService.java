package com.multi.hereevent.wait;

import com.multi.hereevent.dto.WaitDTO;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface WaitService {
    //대기 로그인, 대기 상세정보 불러오기
    WaitDTO getWaitInfo(String wait_tel);

    //대기 추가
    int waitInsert(WaitDTO wait);

    boolean registerWait(WaitDTO wait);
    //대기리스트
    List<WaitDTO> getWaitList();

    //대기 상세 확인
    WaitDTO read(String wait_tel);

    //대기 상세정보 불러오기
    WaitDTO waitDetail(int wait_no);
    //대기로 이벤트 상세정보 불러오기
    WaitDTO eventDetail(int wait_no);

    //대기 삭제
    int waitDelete(int wait_no);

    //대기 순서
    int getWaitingPosition(int event_no,int wait_no);
    int getWaitingCount(int event_no);
    int updateState(WaitDTO wait);
    boolean canInsert(String wait_tel);
    void checkAndUpdateWaitStatus();
    //입장 가능시간
    String getEntranceWaitTime(int event_no, int wait_no);

    //선택업데이트
    int updateStateSelect(List<WaitDTO> waitList);
    Page<WaitDTO> selectWaitWithPage(Map<String, Object> params, Pageable page);

}
