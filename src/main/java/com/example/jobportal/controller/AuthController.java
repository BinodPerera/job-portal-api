package com.example.jobportal.controller;

import com.example.jobportal.dto.AuthResponse;
import com.example.jobportal.dto.LoginRequest;
import com.example.jobportal.dto.RegisterRequest;
import com.example.jobportal.service.AuthService;
import com.example.jobportal.security.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;




@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    private JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            AuthResponse authResponse = authService.login(loginRequest);
            
            Cookie cookie = new Cookie("token", authResponse.getToken());
            cookie.setHttpOnly(true);         // 🛡️ prevent JS access
            cookie.setSecure(false);          // change to true if using HTTPS
            cookie.setPath("/");              // send cookie to all endpoints
            cookie.setMaxAge(24 * 60 * 60);   // 1 day expiry

            response.addCookie(cookie);       // ✅ Set cookie in response

            return ResponseEntity.ok("Login successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    // this method for check if the user logged in. Becase we need to verify authentication before user login sensitive areas
    @GetMapping("/check")
    public ResponseEntity<?> checkAuth(@CookieValue(value = "token", required = false) String token){
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }
        return ResponseEntity.ok("User is authenticated");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // ❌ Expire immediately

        response.addCookie(cookie);
        return ResponseEntity.ok("Logged out successfully");
    }
}
