package com.example.jobportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.jobportal.dto.UserProfile;
import com.example.jobportal.model.User;
import com.example.jobportal.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserProfile getProfile(String username){
        User user = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User not Found!!"));
        UserProfile userProfile = new UserProfile();
        userProfile.setUsername(user.getUsername());
        userProfile.setRole(user.getRole());
        userProfile.setImage(user.getImage());
        return userProfile;
    }

    public Long getUserCount(){
        Long count = userRepository.count();
        return count;
    }
}
