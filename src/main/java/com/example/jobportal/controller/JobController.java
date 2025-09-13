package com.example.jobportal.controller;

// JobController.java
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


import com.example.jobportal.model.Job;
import com.example.jobportal.security.JwtUtil;
import com.example.jobportal.service.JobService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/job")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class JobController {

    private final JobService jobService;
    private final JwtUtil jwtUtil;

    public JobController(JobService jobService, JwtUtil jwtUtil) {
        this.jobService = jobService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createJob(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("category") String category,

            @RequestParam(value = "image", required = false) MultipartFile image,

            @RequestParam(value = "companyId", required = false) Long companyId,
            @RequestParam(value = "companyTitle", required = false) String companyTitle,
            @RequestParam(value = "companyDescription", required = false) String companyDescription,
            @RequestParam(value = "companyLocation", required = false) String companyLocation,
            @RequestParam(value = "companyLogo", required = false) MultipartFile companyLogo
    ) {
        try {
            Job job = jobService.createJob(
                    title, description, category, image,
                    companyId, companyTitle, companyDescription, companyLocation, companyLogo
            );
            return ResponseEntity.ok(Map.of("job", job, "message", "Job created successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
