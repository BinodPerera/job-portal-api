package com.example.jobportal.controller;

import com.example.jobportal.dto.AuthResponse;
import com.example.jobportal.dto.RegisterRequest;
import com.example.jobportal.service.AuthService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "http://localhost:3000")
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

            // ✅ Use absolute path
            String uploadFolder = System.getProperty("user.dir") + "/uploads";
            Path uploadDir = Paths.get(uploadFolder);

            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // ✅ Save file to absolute path
            Files.copy(imageFile.getInputStream(),
                    uploadDir.resolve(imageName),
                    StandardCopyOption.REPLACE_EXISTING);

            // Create RegisterRequest
            RegisterRequest request = new RegisterRequest();
            request.setUsername(username);
            request.setPassword(password);
            request.setRole(role);
            request.setImage(imageName);  // only the file name stored

            return ResponseEntity.ok(authService.register(request));
        } catch (IOException e) {
            e.printStackTrace(); // Still good for debugging
            return ResponseEntity.internalServerError().body(null);
        }

    }

}
