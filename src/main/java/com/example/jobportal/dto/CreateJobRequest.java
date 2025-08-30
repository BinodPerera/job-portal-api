package com.example.jobportal.dto;

import lombok.Data;

@Data
public class CreateJobRequest {
    private String title;
    private String description;
    private String category;
    private String image;
    
    private String companyId;
    private String companyTitle;
    private String companyDescription;
    private String companyLocation;
    private String companyLogo;
}
