package com.multi.hereevent.category;

import com.multi.hereevent.dto.CategoryDTO;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategoryDAOImpl implements CategoryDAO{
    private final SqlSession sqlSession;

    @Override
    public List<CategoryDTO> getListCategory() {
        return sqlSession.selectList("com.multi.hereevent.category.list");
    }

    @Override
    public String selectCategoryName(int category_no) {
        return sqlSession.selectOne("com.multi.hereevent.category.selectCategoryName", category_no);
    }
}
