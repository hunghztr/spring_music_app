package com.whisperdev.music_app.controller;

import com.whisperdev.music_app.dto.StringResult;
import com.whisperdev.music_app.dto.user.LoginResponse;
import com.whisperdev.music_app.model.Role;
import com.whisperdev.music_app.model.User;
import com.whisperdev.music_app.service.RoleService;
import com.whisperdev.music_app.service.UserService;
import com.whisperdev.music_app.utils.SecurityUtil;
import com.whisperdev.music_app.utils.exception.InvalidException;
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

    @PostMapping("/refresh")
    public ResponseEntity<?> getRefreshToken(
//            @CookieValue(name = "refresh_token", defaultValue = "abc") String refresh_token,
            @RequestBody StringResult stringResult)
            throws InvalidException {
//        if (refresh_token.equals("abc")) {
//            throw new InvalidException("Bạn không có refresh token ở cookie");
//        }
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
//
//    @PostMapping("/auth/logout")
//    @ApiMessage("Logout User")
//    public ResponseEntity<Void> logout() throws IdInvalidException {
//        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get()
//                : "";
//
//        if (email.equals("")) {
//            throw new IdInvalidException("Access Token không hợp lệ");
//        }
//
//        // update refresh token = null
//        this.userService.updateUserToken(null, email);
//
//        // remove refresh token cookie
//        ResponseCookie deleteSpringCookie = ResponseCookie
//                .from("refresh_token", null)
//                .httpOnly(true)
//                .secure(true)
//                .path("/")
//                .maxAge(0)
//                .build();
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.SET_COOKIE, deleteSpringCookie.toString())
//                .body(null);
//    }
//
//    @PostMapping("/auth/register")
//    @ApiMessage("Register a new user")
//    public ResponseEntity<ResCreateUserDTO> register(@Valid @RequestBody User postManUser)
//            throws IdInvalidException {
//        boolean isEmailExist = this.userService.isEmailExist(postManUser.getEmail());
//        if (isEmailExist) {
//            throw new IdInvalidException(
//                    "Email " + postManUser.getEmail() + "đã tồn tại, vui lòng sử dụng email khác.");
//        }
//
//        String hashPassword = this.passwordEncoder.encode(postManUser.getPassword());
//        postManUser.setPassword(hashPassword);
//        User ericUser = this.userService.handleCreateUser(postManUser);
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(this.userService.convertToResCreateUserDTO(ericUser));
//    }
}