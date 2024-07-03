package com.multi.hereevent.category;

import com.multi.hereevent.dto.CategoryDTO;

import java.util.List;

public interface CategoryDAO {
    int insertCategory(CategoryDTO category);
    int updateCategory(CategoryDTO category);
    int deleteCategory(CategoryDTO category);
    List<CategoryDTO> selectCategoryByName(); // 대분류 카테고리만 불러오기
    List<CategoryDTO> selectCategoryByValue();// 대분류, 소분류 카테고리 불러오기
    //전체 카테고리 불러오기
    List<CategoryDTO> getListCategory();
    String selectCategoryName(int category_no);
}
