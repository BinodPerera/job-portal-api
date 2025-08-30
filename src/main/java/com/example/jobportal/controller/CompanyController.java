package com.example.jobportal.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jobportal.model.Company;
import com.example.jobportal.repository.CompanyRepository;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@CrossOrigin(origins = "", allowCredentials = "true")
@RequestMapping("/api/company")
public class CompanyController {

    private final CompanyRepository companyRepository;

    public CompanyController(CompanyRepository companyRepository){
        this.companyRepository = companyRepository;
    }

    @GetMapping("/")
    public ResponseEntity<List<Company>> getAllCompanies() {
        try{
            List<Company> companies = companyRepository.findAll();
            if(companies.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(companies);
        } catch (Exception error){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
