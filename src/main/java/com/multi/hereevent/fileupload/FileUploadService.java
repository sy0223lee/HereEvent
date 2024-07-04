package com.multi.hereevent.fileupload;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.multi.hereevent.dto.ReviewImgDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;

@Service
public class FileUploadService {
    // 파일 업로드 경로
    // Naver Cloud 서버 이용하는 경우 해당 서버에 저장될 수 있도록 변경하기 !!
    @Value("C:/hereevent_upload/")
    private String filePath;

    // 파일 경로
    public String getProfileFilePath(String filename) {
        return filePath + "profile/" + filename;
    }
    public String getEventFilePath(String filename) {
        return filePath + "event/" + filename;
    }
    public String getReviewFilePath(String filename) {
        return filePath + "review/" + filename;
    }

    // 리뷰 사진 저장
    public List<ReviewImgDTO> uploadReviewImg(List<MultipartFile> multipartFiles) throws IOException {
        List<ReviewImgDTO> imgList = new ArrayList<ReviewImgDTO>();
        for(MultipartFile multipartFile : multipartFiles) {
            if(!multipartFile.isEmpty()) {
                String storeFilename = "";

                if(!multipartFile.isEmpty()) {
                    String originalFilename = multipartFile.getOriginalFilename();
                    if(originalFilename != null) {
                        storeFilename = createStoreFilename(originalFilename);
                        multipartFile.transferTo(new File(getReviewFilePath(storeFilename)));
                    }
                }

                imgList.add(new ReviewImgDTO(storeFilename));
            }
        }
        return imgList;
    }

    // 프로필 사진 저장
    public String uploadProfileImg(MultipartFile multipartFile) throws IOException {
        String storeFilename = "";

        if(!multipartFile.isEmpty()) {
            String originalFilename = multipartFile.getOriginalFilename();
            if(originalFilename != null) {
                storeFilename = createStoreFilename(originalFilename);
                multipartFile.transferTo(new File(getProfileFilePath(storeFilename)));
            }
        }
        return storeFilename;
    }
    // 이벤트사진 저장
    public String uploadEventImg(MultipartFile multipartFile) throws IOException {
        String storeFilename = "";

        if(!multipartFile.isEmpty()) {
            String originalFilename = multipartFile.getOriginalFilename();
            if(originalFilename != null) {
                storeFilename = createStoreFilename(originalFilename);
                multipartFile.transferTo(new File(getEventFilePath(storeFilename)));
            }
        }
        return storeFilename;
    }


    // 크롤링한 이벤트 사진 저장
    public String uploadEventImg(String imgUrl) throws IOException {
        URL url = new URL(imgUrl);
        int position = imgUrl.lastIndexOf('/');
        String originalFilename = imgUrl.substring(position + 1) + ".png";
        String storeFilename = createStoreFilename(originalFilename);

        BufferedImage img = ImageIO.read(url);
        try {
            ImageIO.write(img, "png", new File(getEventFilePath(storeFilename)));
        } catch (IllegalArgumentException ignored) {
            storeFilename = null;
        }
        return storeFilename;
    }

    // UUID 를 이용해서 파일명 변환
    private String createStoreFilename(String originalFilename) {
        int position = originalFilename.lastIndexOf('.'); // 확장자 위치 구하기
        String ext = originalFilename.substring(position); // ".확장자" 추출
        String uuid = UUID.randomUUID().toString();
        return uuid + ext;
    }
}
