package com.example.jobportal.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.jobportal.model.Company;
import com.example.jobportal.repository.CompanyRepository;

@Service
public class CompanyService {

    // private final CompanyRepository companyRepository;

    // public CompanyService(CompanyRepository companyRepository){
    //     this.companyRepository = companyRepository;
    // }

    // public List<Company> getCompany() throws IOException{
    //     try{
    //         List<Company> companies = companyRepository.findAll();
    //         if(companies.isEmpty()){
    //             throw new RuntimeException("Sorry! Companies not found!");
    //         }
    //         return companies;
    //     } catch (Exception error) {
    //         System.out.println("Error fetching companies: "+ error.getMessage());
    //         throw new RuntimeException("Attention! Error occured when fetch data from repository! Error: " + error.getMessage());
    //     }
    // }
    
}
