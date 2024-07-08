package com.multi.hereevent.admin;

import com.multi.hereevent.dto.ChartDTO;

import java.util.List;

public interface ChartDAO {
    List<ChartDTO> startEndEventCount(); // 날짜별 시작/종료 이벤트
    List<ChartDTO> categoryRate(); // 이벤트 카테고리 비율
    List<ChartDTO> newMemberCount(); // 날짜별 신규 회원 가입
    List<ChartDTO> reserveTopEvent(); // 예약 상위 이벤트
    List<ChartDTO> waitTopEvent(); // 대기 상위 이벤트
}
