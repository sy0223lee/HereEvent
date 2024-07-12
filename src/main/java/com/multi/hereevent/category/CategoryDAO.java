package com.multi.hereevent.category;

import com.multi.hereevent.dto.CategoryDTO;

import java.util.List;

public interface CategoryDAO {
    //전체 카테고리 불러오기
    List<CategoryDTO> getListCategory();
    String selectCategoryName(int category_no);
}
