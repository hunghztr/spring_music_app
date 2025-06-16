package com.whisperdev.music_app.utils.exception;

import com.whisperdev.music_app.dto.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler({InvalidException.class,
            UsernameNotFoundException.class,
            BadCredentialsException.class})
    public ResponseEntity<?> handleException(Exception ex) {
        RestResponse<Object> response = new RestResponse<>();
        response.setMessage(ex.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(response);
    }
}
