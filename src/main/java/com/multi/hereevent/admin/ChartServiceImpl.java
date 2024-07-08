package com.multi.hereevent.admin;

import com.multi.hereevent.dto.ChartDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChartServiceImpl implements ChartService{
    private final ChartDAO chartDAO;
    @Override
    public List<ChartDTO> startEndEventCount() {
        return chartDAO.startEndEventCount();
    }

    @Override
    public List<ChartDTO> categoryRate() {
        return chartDAO.categoryRate();
    }

    @Override
    public List<ChartDTO> newMemberCount() {
        return chartDAO.newMemberCount();
    }

    @Override
    public List<ChartDTO> reserveTopEvent() {
        return chartDAO.reserveTopEvent();
    }

    @Override
    public List<ChartDTO> waitTopEvent() {
        return chartDAO.waitTopEvent();
    }
}
