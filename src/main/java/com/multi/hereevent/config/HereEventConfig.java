package com.multi.hereevent.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class HereEventConfig implements WebMvcConfigurer {
    // 정적 리소스의 경로 설정
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 특정 path 로 요청하면 실제 파일이 저장된 위치를 연결해서 리소스를 가져올 수 있도록 처리
        registry.addResourceHandler("/download/**") // '/download' 로 시작하는 모든 요청에 대해
                .addResourceLocations("file:/root/upload/"); // 실제 위치에서 파일 찾기
    }
}
