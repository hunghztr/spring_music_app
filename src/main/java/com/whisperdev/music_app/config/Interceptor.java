package com.whisperdev.music_app.config;

import com.whisperdev.music_app.model.Permission;
import com.whisperdev.music_app.model.User;
import com.whisperdev.music_app.service.UserService;
import com.whisperdev.music_app.utils.exception.InvalidException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.List;


public class Interceptor implements HandlerInterceptor {
    @Autowired private UserService userService;

    @Override
    @Transactional
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();
        System.out.println(">>> RUN preHandle");
        System.out.println(">>> path= " + path);
        System.out.println(">>> httpMethod= " + httpMethod);
        System.out.println(">>> requestURI= " + requestURI);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.fetchUserByUsername(username);
        if(user != null){
            List<Permission> permissions = user.getRole().getPermissions();
            boolean isAuthenticated = permissions.stream()
                    .anyMatch(i -> i.getMethod().equals(httpMethod) && i.getPath().equals(path));
            if(!isAuthenticated){
                throw new InvalidException("Bạn không có quyền hạn truy cập vào tài nguyên này");
            }
        }else{
            throw new InvalidException("Bạn không có quyền hạn truy cập vào tài nguyên này");
        }
        return true;
    }
}
