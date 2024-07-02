package com.multi.hereevent.review;

import com.multi.hereevent.dto.ReviewDTO;
import com.multi.hereevent.dto.ReviewImgDTO;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public  class ReviewDAOImpl implements ReviewDAO{
    private final SqlSession sqlSession;

    @Override
    public int insertReview(ReviewDTO review) {
        return sqlSession.insert("com.multi.hereevent.review.insertReview", review);
    }

    @Override
    public int insertReviewImg(List<ReviewImgDTO> imgList) {
        return sqlSession.insert("com.multi.hereevent.review.insertReviewImg", imgList);
    }

    @Override
    public int updateReview(ReviewDTO review) {
        return sqlSession.update("com.multi.hereevent.review.updateReview", review);
    }

    @Override
    public int insertReviewImgWithReviewNo(int review_no, List<ReviewImgDTO> imgList) {
        Map<String, Object> params = new HashMap<>();
        params.put("review_no", review_no);
        params.put("img_list", imgList);
        return sqlSession.insert("com.multi.hereevent.review.insertReviewImgWithReviewNo", params);
    }

    @Override
    public int deleteReview(int review_no) {
        return sqlSession.delete("com.multi.hereevent.review.deleteReview", review_no);
    }

    @Override
    public void deleteReviewImg(int review_img_no) {
        sqlSession.delete("com.multi.hereevent.review.deleteReviewImg", review_img_no);
    }

    @Override
    public ReviewDTO selectReviewWithEventImg(int review_no) {
        return sqlSession.selectOne("com.multi.hereevent.review.selectReviewWithEventImg", review_no);
    }

    @Override
    public List<ReviewDTO> selectReviewByEventNo(int event_no) {
        return sqlSession.selectList("com.multi.hereevent.review.selectReviewByEventNo", event_no);
    }

    @Override
    public List<ReviewDTO> selectReviewByMemberNo(int member_no) {
        return sqlSession.selectList("com.multi.hereevent.review.selectReviewByMemberNo", member_no);
    }

    @Override
    public List<ReviewImgDTO> selectReviewImgs(int review_no) {
        return sqlSession.selectList("com.multi.hereevent.review.selectReviewImgs", review_no);
    }

    @Override
    public int countReviewWithPage(Map<String, Object> params) {
        return sqlSession.selectOne("com.multi.hereevent.review.countReviewWithPage", params);
    }

    @Override
    public List<ReviewDTO> selectReviewWithPage(Map<String, Object> params) {
        return sqlSession.selectList("com.multi.hereevent.review.selectReviewWithPage", params);
    }
}
