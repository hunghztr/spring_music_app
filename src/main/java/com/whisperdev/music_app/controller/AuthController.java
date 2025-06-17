package com.whisperdev.music_app.controller;

import com.whisperdev.music_app.dto.user.LoginResponse;
import com.whisperdev.music_app.dto.user.ReqLoginDTO;
import com.whisperdev.music_app.dto.user.ResLoginDTO;
import com.whisperdev.music_app.model.User;
import com.whisperdev.music_app.service.UserService;
import com.whisperdev.music_app.utils.SecurityUtil;
import com.whisperdev.music_app.utils.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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

    @Value("${whisper.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    @PostMapping("/login")
    public ResponseEntity<?> login( @RequestBody User user) {
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
            user.setRole("USER");
            user.setVerify(true);
            userService.save(user);
        }
        LoginResponse loginResponse = userService.getLoginResponse(user.getUsername());

        return ResponseEntity.ok().body(loginResponse);
    }
//    @GetMapping("/auth/account")
//    @ApiMessage("fetch account")
//    public ResponseEntity<ResLoginDTO.UserGetAccount> getAccount() {
//        String email = SecurityUtil.getCurrentUserLogin().isPresent()
//                ? SecurityUtil.getCurrentUserLogin().get()
//                : "";
//
//        User currentUserDB = this.userService.handleGetUserByUsername(email);
//        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
//        ResLoginDTO.UserGetAccount userGetAccount = new ResLoginDTO.UserGetAccount();
//
//        if (currentUserDB != null) {
//            userLogin.setId(currentUserDB.getId());
//            userLogin.setEmail(currentUserDB.getEmail());
//            userLogin.setName(currentUserDB.getName());
//            userLogin.setRole(currentUserDB.getRole());
//
//            userGetAccount.setUser(userLogin);
//        }
//
//        return ResponseEntity.ok().body(userGetAccount);
//    }
//
//    @GetMapping("/auth/refresh")
//    @ApiMessage("Get User by refresh token")
//    public ResponseEntity<ResLoginDTO> getRefreshToken(
//            @CookieValue(name = "refresh_token", defaultValue = "abc") String refresh_token)
//            throws IdInvalidException {
//        if (refresh_token.equals("abc")) {
//            throw new IdInvalidException("Bạn không có refresh token ở cookie");
//        }
//        // check valid
//        Jwt decodedToken = this.securityUtil.checkValidRefreshToken(refresh_token);
//        String email = decodedToken.getSubject();
//
//        // check user by token + email
//        User currentUser = this.userService.getUserByRefreshTokenAndEmail(refresh_token, email);
//        if (currentUser == null) {
//            throw new IdInvalidException("Refresh Token không hợp lệ");
//        }
//
//        // issue new token/set refresh token as cookies
//        ResLoginDTO res = new ResLoginDTO();
//        User currentUserDB = this.userService.handleGetUserByUsername(email);
//        if (currentUserDB != null) {
//            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
//                    currentUserDB.getId(),
//                    currentUserDB.getEmail(),
//                    currentUserDB.getName(),
//                    currentUserDB.getRole());
//            res.setUser(userLogin);
//        }
//
//        // create access token
//        String access_token = this.securityUtil.createAccessToken(email, res);
//        res.setAccessToken(access_token);
//
//        // create refresh token
//        String new_refresh_token = this.securityUtil.createRefreshToken(email, res);
//
//        // update user
//        this.userService.updateUserToken(new_refresh_token, email);
//
//        // set cookies
//        ResponseCookie resCookies = ResponseCookie
//                .from("refresh_token", new_refresh_token)
//                .httpOnly(true)
//                .secure(true)
//                .path("/")
//                .maxAge(refreshTokenExpiration)
//                .build();
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
//                .body(res);
//    }
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