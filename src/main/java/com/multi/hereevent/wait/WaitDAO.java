package com.multi.hereevent.wait;

import com.multi.hereevent.dto.WaitDTO;

import java.util.List;
import java.util.Map;

public interface WaitDAO {
    //대기 확인을 위한 로그인, wait_tel 로 세부정보 가져오기, 기존 레코드 확인
    WaitDTO getWaitInfo(String wait_tel);

    int checkWaitLimit(int event_no);
    int waitInsert(WaitDTO wait);
    int delete(int wait_no); //대기 삭제
    List<WaitDTO> getWaitList(); //대기 리스트 확인
    WaitDTO read(String wait_no); //대기상세조회 - db에 처리
    WaitDTO waitDetail(int wait_no); //wait_no로 세부정보 가져오기
    WaitDTO eventDetails(int wait_no); //wait_no로 event 세부정보 가져오기
    int updateState(WaitDTO wait); //wait_state 변환

    //event_no로 기다리는 순서 추출
    List<WaitDTO> whenIgetInNo(int event_no);
    List<WaitDTO> getAllWaitingList();
    List<WaitDTO> getWaitingListByEventNo(int event_no);

    List<WaitDTO> selectWaitToUpdate(int event_no); // wait_date 수정해야 하는 대기와 메일을 보내야하는 대기 조회
    int updateStateSelect(List<WaitDTO> waitList);

    // DAO
    int countWaitWithPage(Map<String, Object> params);
    List<WaitDTO> selectWaitWithPage(Map<String, Object> params);
}
