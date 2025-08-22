package com.example.jobportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jobportal.dto.UserProfile;
import com.example.jobportal.security.JwtUtil;
import com.example.jobportal.service.UserService;


@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/user")
public class UserController{

    @Autowired
    private JwtUtil jwtUtil;
    private UserService userService;

    public UserController(JwtUtil jwtUtil, UserService userService){
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @GetMapping("/getprofile")
    public ResponseEntity<?> getProfile(
        @CookieValue(value = "token", required = false) String token
    ){
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing token");
        }
        String username = jwtUtil.extractUsername(token);
        UserProfile userProfile = userService.getProfile(username);
        return ResponseEntity.ok(userProfile);
    }

    @GetMapping("/usercount")
    public ResponseEntity<?> getUserCount(@CookieValue(value = "token", required = false) String token) {
        if(token ==null || !jwtUtil.validateToken(token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Restricted! Unauthorise Access");
        }
        Long userCount = userService.getUserCount();
        return ResponseEntity.ok(userCount);
    }
}
