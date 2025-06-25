package com.whisperdev.music_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class interceptorConfig implements WebMvcConfigurer {
    @Bean
    Interceptor getPermissionInterceptor() {
        return new Interceptor();
    }
    private final String[] whiteList = {"/","/api/v1/auth/login","/api/v1/auth/register",
            "/api/v1/trucks/search","/api/v1/", "/api/v1/likes/increase-view","/api/v1/tracks/top",
            "/api/v1/tracks/user", "/api/v1/tracks/get-id/**","/api/v1/tracks",
            "/api/v1/comments/get-by-track", "/api/v1/playlists",
            "/api/v1/auth/social-media","/api/v1/auth/refresh","/api/v1/files/upload-avatar",
            "/api/v1/images/avatar/{name}","/api/v1/images/{name}","/api/v1/tracks/{name}"};
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getPermissionInterceptor())
                .excludePathPatterns(whiteList);
    }
}
