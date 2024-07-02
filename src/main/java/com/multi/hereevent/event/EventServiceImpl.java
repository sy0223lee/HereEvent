package com.multi.hereevent.event;

import com.multi.hereevent.category.CategoryDAO;
import com.multi.hereevent.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventDAO dao;
    private final CategoryDAO categoryDAO;

    @Override
    public int insertEvent(EventDTO event) {
        return dao.insertEvent(event);
    }


    @Override
    public int updateEvent(EventDTO event) {
        return dao.updateEvent(event);

    }
    @Override
    public List<EventDTO> selectAll() {
        return dao.selectAll();
    }

    @Override
    public int deleteEvent(int id) {
        return dao.deleteEvent(id);
    }
    @Override
    public List<EventDTO> searchEvent(String keyword) {
        return dao.searchEvent(keyword);
    }

    @Override
    public List<EventDTO> getAllEvent() {
        return dao.getAllEvent();
    }

    @Override
    public List<EventDTO> getListByStarRank() {
        return dao.getListStarRank();
    }

    @Override
    public List<EventDTO> selectEventByCategoryNo(int category_no) {
        return dao.selectEventByCategoryNo(category_no);
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
            eventlist = dao.selectFourEventByCategory(category.getCategory_no());
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
        return dao.getOpenEvent();
    }

    @Override
    public List<EventDTO> getPopularEvent() {
        return dao.getPopularEvent();
    }


    //세부페이지
    @Override
    public EventDTO getEventDetails(int event_no) {
        return dao.getEventDetails(event_no);
    }

    @Override
    public EventDTO getEventDetails(int event_no, int category_no) {
        return dao.getEventDetails(event_no, category_no);
    }

    //사진 가져오기
    @Override
    public EventDTO getEventImage(int event_no) {
        return dao.getEventImage(event_no);
    }

    @Override
    public int insertReserve(ReserveDTO reservation) {
        return dao.insertReserve(reservation);
    }

    @Override
    public ReserveDTO checkReserveOrder(int event_no,Date reserve_date, Time reserve_time) {
        return dao.checkReserveOrder(event_no,reserve_date,reserve_time);
    }

    @Override
    public int checkReserveLimit(int event_no) {
        return dao.checkReserveLimit(event_no);
    }

    // 크롤링
    @Override
    public int insertCrawlingEvent(EventDTO event) {
        return dao.insertCrawlingEvent(event);
    }

    @Override
    public int updateEventImg(int event_no, String img_path) {
        return dao.updateEventImg(event_no, img_path);
    }

    @Override
    public int selectEventNoByEventName(String eventName) {
        return Integer.parseInt(dao.selectEventNoByEventName(eventName));
    }

    @Override
    public List<MemberEventDTO> selectMemberEvent(int member_no) {
        return dao.selectMemberEvent(member_no);
    }

    @Override
    public List<EventDTO> selectNewEvent(int member_no) {
        return dao.selectNewEvent(member_no);
    }

}