package com.example.jobportal.controller;

import com.example.jobportal.dto.AuthResponse;
import com.example.jobportal.dto.RegisterRequest;
import com.example.jobportal.service.AuthService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("role") String role,
            @RequestParam("image") MultipartFile imageFile) {
        
        try {
            String originalFilename = imageFile.getOriginalFilename();
            String imageName = UUID.randomUUID() + "_" + originalFilename;

            // Save file to local directory
            Files.copy(imageFile.getInputStream(),
                    Paths.get("uploads", imageName),
                    StandardCopyOption.REPLACE_EXISTING);

            // Build RegisterRequest manually
            RegisterRequest request = new RegisterRequest();
            request.setUsername(username);
            request.setPassword(password);
            request.setRole(role);
            request.setImage(imageName);  // store image name only

            return ResponseEntity.ok(authService.register(request));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }


    @PostMapping("/testing")
    public void testing(@RequestBody String username){
        System.out.println("Testing endpoint called with username: " + username);
    }
}
