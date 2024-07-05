package com.multi.hereevent.wait;

import com.multi.hereevent.dto.WaitDTO;

import java.util.List;

public interface WaitDAO {
    //대기 확인을 위한 로그인, wait_tel 로 세부정보 가져오기, 기존 레코드 확인
    WaitDTO getWaitInfo(String wait_tel);
    //대기 등록
    int waitInsert(WaitDTO wait);
    //대기 삭제
    int delete(int wait_no);
    //대기 리스트 확인
    List<WaitDTO> getWaitList();
    //대기상세조회 - db에 처리
    WaitDTO read(String wait_no);
    //wait_no로 세부정보 가져오기
    WaitDTO waitDetail(int wait_no);
    //wait_no로 event세부정보 가져오기
    WaitDTO eventDetails(int wait_no);
    //wait_state변환
    int updateState(WaitDTO wait);
    //event_no로 기다리는 순서 추출
    List<WaitDTO> whenIgetInNo(int event_no);
    List<WaitDTO> getAllWaitingList();
    List<WaitDTO> getWaitingListByEventNo(int event_no);
}
