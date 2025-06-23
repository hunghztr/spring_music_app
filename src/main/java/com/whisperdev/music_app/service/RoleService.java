package com.whisperdev.music_app.service;

import com.whisperdev.music_app.model.Role;
import com.whisperdev.music_app.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {
    @Autowired private RoleRepository roleRepository;
    public Role fetchByName(String name){
        Optional<Role> role = roleRepository.findByName(name);
        if(role.isPresent()){
            return role.get();
        }
        return null;
    }
}
