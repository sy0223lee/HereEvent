package com.multi.hereevent.review;

import com.multi.hereevent.dto.MemberDTO;
import com.multi.hereevent.dto.ReviewDTO;
import com.multi.hereevent.dto.ReviewImgDTO;
import com.multi.hereevent.event.EventService;
import com.multi.hereevent.fileupload.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@SessionAttributes("member")
public class ReviewController {
    private final ReviewService reviewService;
    private final FileUploadService fileUploadService;

    // 이벤트 상세페이지에서 리뷰 조회
    // EventController에 작성

    // 이벤트 상세페이지에서 리뷰 작성
    @PostMapping("/review/insert")
    public String insertReview(ReviewDTO review) throws IOException {
        List<MultipartFile> fileList = review.getFiles();
        List<ReviewImgDTO> imgList = fileUploadService.uploadReviewImg(fileList);
        int result = reviewService.insertReview(review, imgList);
        if(result > 0){
            return "redirect:/event/" + review.getEvent_no();
        }else {
            return "common/errorPage";
        }
    }
    @PostMapping("/event/review/delete")
    public String deleteReview(@RequestParam("review_no") String review_no, @RequestParam("event_no") String event_no){
        int result = reviewService.deleteReview(Integer.parseInt(review_no));
        if(result > 0){
            return "redirect:/event/" + event_no;
        }else {
            return "common/errorPage";
        }
    }

    // 마이페이지에서 리뷰 조회
    @GetMapping("/myreview")
    public String myReviewPage(Model model){
        MemberDTO member = (MemberDTO) model.getAttribute("member");
        assert member != null;
        List<ReviewDTO> reviewList = reviewService.selectReviewByMemberNo(member.getMember_no());
        model.addAttribute("reviewList", reviewList);
        return "mypage/myreview";
    }

    // 마이페이지 리뷰 수정과 삭제
    @GetMapping("/myreview/update")
    public String updateReviewPage(@RequestParam("review_no") String review_no, Model model){
        ReviewDTO review = reviewService.selectReviewWithEventImg(Integer.parseInt(review_no));
        model.addAttribute("review", review);
        return "mypage/editReview";
    }
    @PostMapping("/myreview/update")
    public String updateReview(ReviewDTO review) throws IOException {
        List<MultipartFile> fileList = review.getFiles();
        List<ReviewImgDTO> imgList = fileUploadService.uploadReviewImg(fileList);
        int result = reviewService.updateReview(review, imgList);
        if(result > 0){
            return "redirect:/myreview";
        }else {
            return "common/errorPage";
        }
    }
    @PostMapping("/myreview/delete")
    public String deleteReview(@RequestParam("review_no") String review_no){
        int result = reviewService.deleteReview(Integer.parseInt(review_no));
        if(result > 0){
            return "redirect:/myreview";
        }else {
            return "common/errorPage";
        }
    }
    @PostMapping("/myreview/delete/img")
    @ResponseBody
    public List<ReviewImgDTO> deleteReviewImg(@RequestParam("review_img_no") String review_img_no,
                                                @RequestParam("review_no") String review_no){
        // 리뷰 이미지 삭제
        reviewService.deleteReviewImg(Integer.parseInt(review_img_no));
        // 리뷰 이미지 정보 다시 받아서 전송
        return reviewService.selectReviewImgs(Integer.parseInt(review_no));
    }

    /***** 관리자 페이지 *****/
    @GetMapping("/admin/review")
    public String selectReviewWithPage(@RequestParam Map<String, Object> params,
                                       @PageableDefault(value = 10) Pageable page, Model model){
        Page<ReviewDTO> result = reviewService.selectReviewWithPage(params, page);
        model.addAttribute("type", params.get("type"));
        model.addAttribute("keyword", params.get("keyword"));
        model.addAttribute("reviewList", result.getContent());
        model.addAttribute("totalPages", result.getTotalPages());
        model.addAttribute("totalElements", result.getTotalElements());
        model.addAttribute("pageNumber", page.getPageNumber());
        return "admin/review";
    }
    /*상세보기 삭제버튼*/
    @PostMapping("/admin/review/delete")
    public String deleteAdminReview(@RequestParam("review_no") String review_no){
        int result = reviewService.deleteReview(Integer.parseInt(review_no));
        if(result > 0){
            return "redirect:/admin/review";
        }else {
            return "common/errorPage";
        }
    }
}
