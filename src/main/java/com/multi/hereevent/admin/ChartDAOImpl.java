package com.multi.hereevent.admin;

import com.multi.hereevent.dto.ChartDTO;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChartDAOImpl implements ChartDAO{
    private final SqlSession sqlSession;

    @Override
    public List<ChartDTO> startEndEventCount() {
        return sqlSession.selectList("com.multi.hereevent.admin.ChartDAO.startEndEventCount");
    }

    @Override
    public List<ChartDTO> categoryRate() {
        return sqlSession.selectList("com.multi.hereevent.admin.ChartDAO.categoryRate");
    }

    @Override
    public List<ChartDTO> newMemberCount() {
        return sqlSession.selectList("com.multi.hereevent.admin.ChartDAO.newMemberCount");
    }

    @Override
    public List<ChartDTO> reserveTopEvent() {
        return sqlSession.selectList("com.multi.hereevent.admin.ChartDAO.reserveTopEvent");
    }

    @Override
    public List<ChartDTO> waitTopEvent() {
        return sqlSession.selectList("com.multi.hereevent.admin.ChartDAO.waitTopEvent");
    }
}
