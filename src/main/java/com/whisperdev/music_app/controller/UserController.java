package com.whisperdev.music_app.controller;

import com.whisperdev.music_app.dto.StringResult;
import com.whisperdev.music_app.dto.page.PageResponse;
import com.whisperdev.music_app.dto.user.UserRequest;
import com.whisperdev.music_app.dto.user.UserResponse;
import com.whisperdev.music_app.model.User;
import com.whisperdev.music_app.service.UserService;
import com.whisperdev.music_app.utils.exception.InvalidException;
import com.whisperdev.music_app.utils.mapper.UserMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<?> getAllUsers(
            @RequestParam(name = "curremt", defaultValue = "1") Integer current,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "username",defaultValue = "temp") String username
    ) {
        PageResponse<UserResponse> pageResponse = userService.getALl(current, pageSize,username);
        return ResponseEntity.ok(pageResponse);
    }

    @PostMapping
    public ResponseEntity<?> addUser(@Valid @RequestBody UserRequest user) throws InvalidException {
        if (userService.isExistByUsername(user.getUsername())) {
            throw new InvalidException("Người dùng đã tồn tại");
        }
        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        UserResponse newUser = userService.register(user);
        return ResponseEntity.ok(newUser);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserRequest user) throws InvalidException {
        userService.update(user);
        StringResult stringResult = new StringResult();
        stringResult.setResult("Cập nhật thành công");
        return ResponseEntity.ok(stringResult);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") String id) {
        userService.deleteById(id);
        StringResult stringResult = new StringResult();
        stringResult.setResult("Xóa thành công");
        return ResponseEntity.ok(stringResult);
    }
    @GetMapping("/search")
    public ResponseEntity<?> searchUser(@RequestParam(name = "username",defaultValue = "") String name,
                                        @RequestParam(name = "current",defaultValue = "1") Integer current,
                                        @RequestParam(name = "pageSize",defaultValue = "2") Integer pageSize) {
        PageResponse<UserResponse> pageResponse = userService.searchUsersByUserame(name, current, pageSize);
        return ResponseEntity.ok(pageResponse);
    }
}
