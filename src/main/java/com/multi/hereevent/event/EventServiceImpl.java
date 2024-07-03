package com.multi.hereevent.event;

import com.multi.hereevent.category.CategoryDAO;
import com.multi.hereevent.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventDAO eventDAO;
    private final CategoryDAO categoryDAO;

    @Override
    public int insertEvent(EventDTO event) {
        return eventDAO.insertEvent(event);
    }
    @Override
    public int updateEvent(EventDTO event) {
        return eventDAO.updateEvent(event);
    }
    @Override
    public int deleteEvent(int id) {
        return eventDAO.deleteEvent(id);
    }
    @Override
    public List<EventDTO> selectAll() {
        return eventDAO.selectAll();
    }

    @Override
    public List<EventDTO> searchEvent(String keyword) {
        return eventDAO.searchEvent(keyword);
    }

    // 메인 페이지 이벤트 리스트 조회
    @Override
    public List<EventDTO> getAllEvent() {
        return eventDAO.getAllEvent();
    }
    @Override
    public List<EventDTO> getListByStarRank() {
        return eventDAO.getListStarRank();
    }
    @Override
    public List<EventDTO> selectEventByCategoryNo(int category_no) {
        return eventDAO.selectEventByCategoryNo(category_no);
    }
    @Override
    public List<FourEventByCategoryDTO> selectFourEventByCategory() {
        // list로 카테고리번호를 가져옴
        List<CategoryDTO> categoryList = categoryDAO.getListCategory();
        //System.out.println("catelist=====>"+categoryList);

        List<FourEventByCategoryDTO> fourList = new ArrayList<>();

        // 가져온 list를 for문 돌리면서
        for(CategoryDTO category : categoryList){
            FourEventByCategoryDTO fourEventDTO= new FourEventByCategoryDTO();
            List<EventDTO> eventlist = new ArrayList<>();
            // sql문으로 가져온 fourEventCategoryDTO를 저장
            eventlist = eventDAO.selectFourEventByCategory(category.getCategory_no());
            //System.out.println("eventlist=====>"+eventlist.size());
            // category_no로 event 4개 조회해서 fourEventCategoryDTO에 저장
            fourEventDTO.setCategory_no(category.getCategory_no());
            fourEventDTO.setName(category.getName());
            fourEventDTO.setEventList(eventlist);
            fourList.add(fourEventDTO);
            //System.out.println("service::fourList=====>"+fourList);
        }

        return fourList;
    }
    @Override
    public List<EventDTO> getOpenEvent() {
        return eventDAO.getOpenEvent();
    }
    @Override
    public List<EventDTO> getPopularEvent() {
        return eventDAO.getPopularEvent();
    }

    // 리스트 페이지 이벤트 리스트 조회
    @Override
    public List<EventDTO> getAllEventWithCondition(List<String> state, List<String> type) {
        return eventDAO.getAllEventWithCondition(state, type);
    }
    @Override
    public List<EventDTO> getStarEventWithCondition(List<String> state, List<String> type) {
        return eventDAO.getStarEventWithCondition(state, type);
    }
    @Override
    public List<EventDTO> getEventByCategoryWithCondition(int category_no, List<String> state, List<String> type) {
        return eventDAO.getEventByCategoryWithCondition(category_no, state, type);
    }
    @Override
    public List<EventDTO> getOpenEventWithCondition(List<String> type) {
        return eventDAO.getOpenEventWithCondition(type);
    }
    @Override
    public List<EventDTO> getPopularEventWithCondition(List<String> state, List<String> type) {
        return eventDAO.getPopularEventWithCondition(state, type);
    }

    //세부페이지
    @Override
    public EventDTO getEventDetails(int event_no) {
        return eventDAO.getEventDetails(event_no);
    }
    @Override
    public EventDTO getEventDetails(int event_no, int category_no) {
        return eventDAO.getEventDetails(event_no, category_no);
    }
    //사진 가져오기
    @Override
    public EventDTO getEventImage(int event_no) {
        return eventDAO.getEventImage(event_no);
    }

    // 리뷰 관련
    @Override
    public int insertReserve(ReserveDTO reservation) {
        return eventDAO.insertReserve(reservation);
    }
    @Override
    public ReserveDTO checkReserveOrder(int event_no,Date reserve_date, Time reserve_time) {
        return eventDAO.checkReserveOrder(event_no,reserve_date,reserve_time);
    }
    @Override
    public int checkReserveLimit(int event_no) {
        return eventDAO.checkReserveLimit(event_no);
    }

    // 크롤링
    @Override
    public int insertCrawlingEvent(EventDTO event) {
        return eventDAO.insertCrawlingEvent(event);
    }
    @Override
    public int updateEventImg(int event_no, String img_path) {
        return eventDAO.updateEventImg(event_no, img_path);
    }
    @Override
    public int selectEventNoByEventName(String eventName) {
        return Integer.parseInt(eventDAO.selectEventNoByEventName(eventName));
    }

    // 특정 멤버 이벤트 내역 조회
    @Override
    public List<MemberEventDTO> selectMemberEvent(int member_no) {
        return eventDAO.selectMemberEvent(member_no);
    }
    // 오늘로부터 2주 내에 오픈 예정인 관심 카테고리 이벤트 조회
    @Override
    public List<EventDTO> selectNewEvent(int member_no) {
        return eventDAO.selectNewEvent(member_no);
    }

    // 페이징
    @Override
    public Page<EventDTO> selectEventWithPage(Map<String, Object> params, Pageable page) {
        int count = eventDAO.countEventWithPage(params);
        params.put("offset", page.getOffset());
        params.put("pageSize", page.getPageSize());
        List<EventDTO> eventList = eventDAO.selectEventWithPage(params);
        return new PageImpl<>(eventList, page, count);
    }
}