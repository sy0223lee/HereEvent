package com.multi.hereevent.review;

import com.multi.hereevent.dto.ReviewDTO;
import com.multi.hereevent.dto.ReviewImgDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ReviewDAO {
    int insertReview(ReviewDTO review); // 리뷰 등록
    int insertReviewImg(List<ReviewImgDTO> imgList); // 리뷰 이미지 등록
    int updateReview(ReviewDTO review); // 리뷰 수정
    int insertReviewImgWithReviewNo(int review_no, List<ReviewImgDTO> imgList); // 리뷰 이미지 등록
    int deleteReview(int review_no); // 리뷰 삭제
    void deleteReviewImg(int review_img_no); // 리뷰 이미지 삭제
    List<ReviewDTO> selectAll(); // 모든 리뷰 조회
    ReviewDTO selectReviewWithEventImg(int review_no); // 리뷰 번호로 리뷰 조회
    List<ReviewDTO> selectReviewByEventNo(int event_no); // 이벤트 번호에 해당하는 리뷰 조회
    List<ReviewDTO> selectReviewByMemberNo(int member_no); // 회원 번호에 해당하는 리뷰 조회
    List<ReviewImgDTO> selectReviewImgs(int review_no);

    // 페이징 처리
    int countReviewWithPage(Map<String, Object> params);
    List<ReviewDTO> selectReviewWithPage(Map<String, Object> params);
}
