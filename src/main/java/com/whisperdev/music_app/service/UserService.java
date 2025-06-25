package com.whisperdev.music_app.service;

import com.whisperdev.music_app.dto.page.MetaResponse;
import com.whisperdev.music_app.dto.page.PageResponse;
import com.whisperdev.music_app.dto.track.TrackResponse;
import com.whisperdev.music_app.dto.user.LoginResponse;
import com.whisperdev.music_app.dto.user.UserRequest;
import com.whisperdev.music_app.dto.user.UserResponse;
import com.whisperdev.music_app.model.Role;
import com.whisperdev.music_app.model.User;
import com.whisperdev.music_app.repository.RoleRepository;
import com.whisperdev.music_app.repository.UserRepository;
import com.whisperdev.music_app.utils.SecurityUtil;
import com.whisperdev.music_app.utils.mapper.UserMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired private UserRepository userRepository;
    @Autowired private SecurityUtil securityUtil;
    @Autowired private RoleRepository roleRepository;
    public User fetchUserByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }
    public User handleUpdateUser(User user, String id) {
        Optional<User> optional = userRepository.findById(id);
        if (optional.isPresent()) {
            User curUser = optional.get();
            curUser.setName(user.getName());
            curUser.setPassword(user.getPassword());
            curUser.setRole(user.getRole());
            return userRepository.save(curUser);
        }
        return null;
    }

    public void updateUserToken(String token, String email) {
        User currentUser = this.fetchUserByUsername(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }
    public boolean isExistByUsername(String username) {
        return this.userRepository.existsByUsername(username);
    }
    public User save(User user) {
        return this.userRepository.save(user);
    }
    public LoginResponse getLoginResponse(String username){
        LoginResponse response = new LoginResponse();
        User currentUserDB = this.fetchUserByUsername(username);
        if(currentUserDB != null) {
            response.setUser(UserMapper.toUserResponse(currentUserDB,""));
        }

        // create access token
        String accessToken = this.securityUtil.generateToken(currentUserDB,"access_token");
        response.setAccessToken(accessToken);

        // create refresh token
        String refreshToken = this.securityUtil.generateToken(currentUserDB,"refresh_token");
        response.setRefreshToken(refreshToken);
        // update user
        this.updateUserToken(refreshToken, currentUserDB.getUsername());
        return  response;
    }

    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndUsername(token, email);
    }

    public UserResponse register(UserRequest user) {
        Role role = roleRepository.findByName("USER").get();
        User register = new User();
        register.setUsername(user.getUsername());
        register.setPassword(user.getPassword());
        register.setRole(role);
        register.setName(user.getName());
        register.setAge(user.getAge());
        register.setGender(user.getGender());
        register.setAddress(user.getAddress());
        if(user.getAvatar() != null && !user.getAvatar().isEmpty()){
            register.setAvatar(user.getAvatar());
        }
        register.setRole(role);
        return UserMapper.toUserResponse(this.userRepository.save(register),"");
    }

    public PageResponse<UserResponse> getALl(Integer current, Integer pageSize,String username) {
        Pageable pageable = PageRequest.of(current-1, pageSize);
        Page<User> page = this.userRepository.findAll(pageable);
        if(!username.equals("temp")){
            page = this.userRepository.findByUsernameContainingIgnoreCase(username, pageable);
        }
        PageResponse<UserResponse> response = new PageResponse<>();
        MetaResponse meta = new MetaResponse();

        meta.setCurrent(pageable.getPageNumber()+1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(page.getTotalPages());

        meta.setTotal(page.getTotalElements());

        response.setMeta(meta);
        List<UserResponse> users = page.getContent().stream().map(i -> UserMapper.toUserResponse(i,"full")).toList();
        response.setResult(users);
        return response;
    }


    public UserResponse update(UserRequest user) {
        User userDB = userRepository.findById(user.getId()).get();
        userDB.setUsername(user.getUsername());
        userDB.setName(user.getName());
        userDB.setAge(user.getAge());
        userDB.setGender(user.getGender());
        userDB.setAddress(user.getAddress());
        userDB = userRepository.save(userDB);
        return UserMapper.toUserResponse(userDB,"");
    }

    public void deleteById(String id) {
         userRepository.deleteById(id);
    }

    public User fetchUserByName(String name) {
        return null;
    }

    public PageResponse<UserResponse> searchUsersByUserame(String username,Integer current, Integer pageSize) {
        Pageable pageable = PageRequest.of(current-1, pageSize);
        Page<User> page = userRepository.findByUsernameContainingIgnoreCase(username,pageable);
        List<UserResponse> userResponses = page.getContent()
                .stream().map(i -> UserMapper.toUserResponse(i,"full")).toList();
        PageResponse<UserResponse> response = new PageResponse<>();
        MetaResponse meta = new MetaResponse();

        meta.setCurrent(pageable.getPageNumber()+1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(page.getTotalPages());

        meta.setTotal(page.getTotalElements());

        response.setMeta(meta);
        response.setResult(userResponses);
        return response;
    }
}
