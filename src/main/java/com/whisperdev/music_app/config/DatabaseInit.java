package com.whisperdev.music_app.config;

import com.whisperdev.music_app.model.Permission;
import com.whisperdev.music_app.model.Role;
import com.whisperdev.music_app.model.User;
import com.whisperdev.music_app.repository.PermissionRepository;
import com.whisperdev.music_app.repository.RoleRepository;
import com.whisperdev.music_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DatabaseInit implements CommandLineRunner {
    @Autowired private PermissionRepository permissionRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        long countPermission = permissionRepository.count();
        long countRole = roleRepository.count();
        long countUser = userRepository.count();
        List<Permission> permissions = new ArrayList<>();
        List<Permission> userPermissions = new ArrayList<>();
        List<Role> roles = new ArrayList<>();

        if(countPermission == 0){
            permissions.add(new Permission("get user by id","GET","/api/v1/users/{id}"));
            permissions.add(new Permission("get all users","GET","/api/v1/users"));
            permissions.add(new Permission("create user","POST","/api/v1/users"));
            permissions.add(new Permission("update user","PUT","/api/v1/users"));
            permissions.add(new Permission("delete user","DELETE","/api/v1/users/{id}"));
            permissions.add(new Permission("find user by username","GET","/api/v1/users/search"));

            permissions.add(new Permission("delete track","DELETE","/api/v1/tracks/{id}"));
            userPermissions.add(new Permission("create track","POST","/api/v1/tracks"));
            userPermissions.add(new Permission("get tracks by user","POST","/api/v1/tracks/user"));

            userPermissions.add(new Permission("create empty playlist","POST","/api/v1/playlists/empty"));
            userPermissions.add(new Permission("update playlist","PUT","/api/v1/playlists"));
            userPermissions.add(new Permission("get playlists by user","POST","/api/v1/playlists/by-user"));

            userPermissions.add(new Permission("get tracks by user like","GET","/api/v1/likes"));
            userPermissions.add(new Permission("interact track","POST","/api/v1/likes"));
            userPermissions.add(new Permission("upload file mp3","POST","/api/v1/files/upload-mp3"));
            userPermissions.add(new Permission("upload file image","POST","/api/v1/files/upload-img"));

            userPermissions.add(new Permission("create comment","POST","/api/v1/comments"));
            userPermissions.add(new Permission("get comments by track","POST","/api/v1/comments/get-by-track"));

            permissionRepository.saveAll(permissions);
            permissionRepository.saveAll(userPermissions);
        }
        if(countRole == 0){
            Role roleAdmin = new Role();
            roleAdmin.setName("ADMIN");
            roleAdmin.setPermissions(permissionRepository.findAll());
            Role roleUser = new Role();
            roleUser.setName("USER");
            roleUser.setPermissions(userPermissions);
            roles.add(roleAdmin);
            roles.add(roleUser);
            roleRepository.saveAll(roles);
        }
        if(countUser == 0){
            User user = new User();
            user.setUsername("admin@gmail.com");
            user.setPassword(passwordEncoder.encode("123"));
            user.setRole(roles.get(0));
            userRepository.save(user);
        }
    }
}
