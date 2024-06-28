package com.multi.hereevent.event;

import com.multi.hereevent.dto.EventDTO;
import com.multi.hereevent.dto.FourEventByCategoryDTO;
import com.multi.hereevent.dto.ReserveDTO;


import java.sql.Date;
import java.sql.Time;
import java.util.List;

public interface EventService {
    //관리자페이지
    //행사 등록
    int insertEvent(EventDTO event);
    int updateEvent(EventDTO event);
    int deleteEvent(int event_no);
    //모든 이벤트 가져오기
    List<EventDTO> selectAll();
    //행사이름 검색
    List<EventDTO> searchEvent(String keyword);
    //전체 팝업 조회
    List<EventDTO> getAllEvent();

    //별점 높은순 10순위 리스트
    List<EventDTO> getListByStarRank();
    //카테고리별 조회
    List<EventDTO> selectEventByCategoryNo(int category_no);
    //카테고리별 4개씩 조회
    List<FourEventByCategoryDTO> selectFourEventByCategory();
    //오픈예정 행사
    List<EventDTO> getOpenEvent();
    //예약,대기 높은순 10순위 리스트
    List<EventDTO> getPopularEvent();


    //세부페이지
    EventDTO getEventDetails(int event_no);
    EventDTO getEventDetails(int event_no, int category_no); // 이벤트 상세 정보 + 회원 관심 여부 조회
    //event image
    EventDTO getEventImage(int event_no);
  
    //예약하기
    int insertReserve(ReserveDTO reservation);
    //예약 순서 체크
    ReserveDTO checkReserveOrder(int event_no, Date reserve_date, Time reserve_time);
    //예약 인원 체크
    int checkReserveLimit(int event_no);
    // 크롤링
    int insertCrawlingEvent(EventDTO event);
    int updateEventImg(int event_no, String img_path);
    int selectEventNoByEventName(String eventName);
}
