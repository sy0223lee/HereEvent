package com.multi.hereevent.category;

import com.multi.hereevent.dto.CategoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{
    private final CategoryDAO dao;

    @Override
    public int insertCategory(CategoryDTO category) {
        return 0;
    }

    @Override
    public int updateCategory(CategoryDTO category) {
        return 0;
    }

    @Override
    public int deleteCategory(CategoryDTO category) {
        return 0;
    }

    @Override
    public List<CategoryDTO> selectCategoryByName() {
        return List.of();
    }

    @Override
    public List<CategoryDTO> selectCategoryByValue() {
        return List.of();
    }

    @Override
    public List<CategoryDTO> getListCategory() {
       return dao.getListCategory();
    }

    @Override
    public String selectCategoryName(int category_no) {
        return dao.selectCategoryName(category_no);
    }
}
