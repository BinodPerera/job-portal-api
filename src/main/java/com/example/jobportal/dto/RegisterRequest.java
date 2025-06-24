package com.example.jobportal.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String image;
    private String role;
}
