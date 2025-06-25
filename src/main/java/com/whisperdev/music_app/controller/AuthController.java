package com.whisperdev.music_app.controller;

import com.whisperdev.music_app.dto.StringResult;
import com.whisperdev.music_app.dto.user.LoginResponse;
import com.whisperdev.music_app.dto.user.UserRequest;
import com.whisperdev.music_app.dto.user.UserResponse;
import com.whisperdev.music_app.model.Role;
import com.whisperdev.music_app.model.User;
import com.whisperdev.music_app.service.RoleService;
import com.whisperdev.music_app.service.UserService;
import com.whisperdev.music_app.utils.SecurityUtil;
import com.whisperdev.music_app.utils.exception.InvalidException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private  AuthenticationManagerBuilder authenticationManagerBuilder;
    @Autowired
    private  SecurityUtil securityUtil;
    @Autowired
    private  UserService userService;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    RoleService roleService;
    @Value("${whisper.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    @PostMapping("/login")
    public ResponseEntity<?> login( @RequestBody User user) {
        log.info(user.getUsername()+"=="+user.getPassword());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user.getUsername(), user.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        LoginResponse loginResponse = userService.getLoginResponse(user.getUsername());
        // set cookies
        ResponseCookie resCookies = ResponseCookie
                .from("refresh_token", loginResponse.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(loginResponse);
    }

    @PostMapping("/social-media")
    public ResponseEntity<?> socialMedia( @RequestBody User user) {
        if(userService.isExistByUsername(user.getUsername())) {
            User getUser = userService.fetchUserByUsername(user.getUsername());
            getUser.setType(user.getType());
            getUser.setVerify(true);
            userService.save(getUser);
        }else{
            Role role = roleService.fetchByName("USER");
            user.setRole(role);
            user.setVerify(true);
            userService.save(user);
        }
        LoginResponse loginResponse = userService.getLoginResponse(user.getUsername());

        return ResponseEntity.ok()
                .body(loginResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> getRefreshToken(
            @RequestBody StringResult stringResult)
            throws InvalidException {

        // check valid
        Jwt decodedToken = this.securityUtil.checkValidRefreshToken(stringResult.getResult());
        String email = decodedToken.getSubject();

        // check user by token + email
        User currentUser = this.userService.getUserByRefreshTokenAndEmail(stringResult.getResult(), email);
        if (currentUser == null) {
            throw new InvalidException("Refresh Token không hợp lệ");
        }
        LoginResponse loginResponse = userService.getLoginResponse(email);
        // lấy refresh token cũ
        loginResponse.setRefreshToken(stringResult.getResult());
        currentUser.setRefreshToken(stringResult.getResult());
        userService.save(currentUser);

        return ResponseEntity.ok()
                .body(loginResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRequest user) throws InvalidException {
        if(userService.isExistByUsername(user.getUsername())) {
            throw new InvalidException("Người dùng đã tồn tại");
        }
        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        userService.register(user);
        StringResult result = new StringResult();
        result.setResult("Đăng kí thành công");
        return ResponseEntity.ok().body(result);
    }
}