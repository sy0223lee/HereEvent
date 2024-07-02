package com.multi.hereevent.event.crawling;

import com.multi.hereevent.dto.EventDTO;
import com.multi.hereevent.dto.EventTimeDTO;
import com.multi.hereevent.event.EventService;
import com.multi.hereevent.event.time.EventTimeService;
import com.multi.hereevent.fileupload.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CrawlingService {
    private final EventService eventService;
    private final EventTimeService eventTimeService;
    private final FileUploadService fileUploadService;

    public void insertEventInfo() {
        // WebDriver 경로 설정
        System.setProperty("webdriver.chrome.driver", "src/main/resources/driver/chromedriver.exe");

        // WebDriver option 설정
        ChromeOptions options = new ChromeOptions();
//        options.addArguments("headless"); // 브라우저 띄우지 않기
        options.addArguments("--disable-popup-blocking"); // 팝업창 무시

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));

        List<EventDTO> eventList = new ArrayList<>(); // 이벤트 정보 저장할 리스트
        List<String> eventImgList = new ArrayList<>(); // 이벤트 이미지 url 저장할 리스트
        List<String> eventDetailList = new ArrayList<>(); // 이벤트 상세 정보 url 저장할 리스트

        // URL 에 넣을 date 계산
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());
        Calendar cal = Calendar.getInstance(Locale.KOREA);
        cal.add(Calendar.DATE, 14); // 2주
        String day = sdf.format(cal.getTime());
        try {
            driver.get("https://popply.co.kr/popup?area=all&startDate=" + day + "&endDate=" + today + "&status=all");
            WebElement element = driver.findElement(By.cssSelector("div.calendar-popup-list.popuplist-board"));

            // 팝업 이미지, 상세 정보 리스트에 저장
            List<WebElement> elements = element.findElements(By.className("popup-img-wrap"));
            for (WebElement e : elements) {
                EventDTO event = new EventDTO();
                eventImgList.add(e.findElement(By.tagName("img")).getAttribute("src")); // 이미지 경로 저장
                eventDetailList.add(e.getAttribute("href")); // 상세 페이지 경로 저장
            }

            int len = elements.size();
            for(int i=0; i<len; i++) {
                // 이벤트 정보 가져오기
                String detailUrl = eventDetailList.get(i);
                driver.get(detailUrl);
                element = driver.findElement(By.cssSelector(".popupdetail-wrap"));

                String name = element.findElement(By.cssSelector("h1.tit")).getText(); // 이벤트 이름
                String addr = element.findElement(By.cssSelector("p.location")).getText(); // 위치
                String info = element.findElement(By.cssSelector(".popupdetail-info-inner")).getText(); // 이벤트 소개

                String[] date = element.findElement(By.cssSelector("p.date")).getText().split(" - "); // 이벤트 운영 기간
                Date startDate = new SimpleDateFormat("yy.MM.dd").parse(date[0]); // 시작일자
                Date endDate = new SimpleDateFormat("yy.MM.dd").parse(date[1]); // 종료일자

                // 가져온 정보로 EventDTO 객체 생성
                EventDTO event = new EventDTO(name, new java.sql.Date(startDate.getTime()), new java.sql.Date(endDate.getTime()), addr, info);

                // 이벤트 이름, 기간이 동일한 이벤트가 이미 존재하는 경우 삽입 X, 존재하지 않는 경우 삽입
                // 새로운 이벤트가 삽입된 경우 이미지 경로 설정해주고 이벤트 운영 시간도 삽입
                if(eventService.insertCrawlingEvent(event) > 0) {
                    // 이벤트 이미지 저장 후 경로 가져오기
                    String imgUrl = eventImgList.get(i);
                    String imgPath = null;
                    if(imgUrl.startsWith("https")){
                        imgPath = fileUploadService.uploadEventImg(imgUrl);
                    }
                    // 가져온 이미지 경로 DB에 저장
                    int eventNo = eventService.selectEventNoByEventName(name); // select event 한 결과값
                    eventService.updateEventImg(eventNo, imgPath);

                    List<EventTimeDTO> eventTimeList = new ArrayList<>();

                    element = driver.findElement(By.cssSelector("ul.open"));
                    List<WebElement> timeList = element.findElements(By.tagName("li"));
                    for(int j=1; j<8; j++){
                        String text = timeList.get(j).getText();
                        String[] dayTime = text.split(" : ");

                        if(dayTime[1].equals("휴무")){
                            eventTimeList.add(new EventTimeDTO(eventNo, dayTime[0], null, null));
                        }else{
                            String[] times = dayTime[1].split(" ~ ");
                            eventTimeList.add(new EventTimeDTO(eventNo, dayTime[0], times[0], times[1]));
                        }
                    }

                    eventTimeService.insertEventTimeList(eventTimeList);
                }
            }
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            driver.quit();
        }
    }
}