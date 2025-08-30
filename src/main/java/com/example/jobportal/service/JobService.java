package com.example.jobportal.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.jobportal.model.Company;
import com.example.jobportal.model.Job;
import com.example.jobportal.repository.CompanyRepository;
import com.example.jobportal.repository.JobRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;

    public JobService(JobRepository jobRepository, CompanyRepository companyRepository) {
        this.jobRepository = jobRepository;
        this.companyRepository = companyRepository;
    }

    public Job createJob(
            String title,
            String description,
            String category,
            MultipartFile image,
            Long companyId,
            String companyTitle,
            String companyDescription,
            String companyLocation,
            MultipartFile companyLogo
    ) throws IOException {

        Company company;

        if (companyId != null) {
            // If companyId is given → fetch by ID
            company = companyRepository.findById(companyId)
                    .orElseThrow(() -> new RuntimeException("Company not found"));
        } else {
            // Check by company title first
            company = companyRepository.findByTitle(companyTitle).orElse(null);

            if (company == null) {
                // Create new company if not found
                String logoFilename = null;
                if (companyLogo != null && !companyLogo.isEmpty()) {
                    logoFilename = saveFile(companyLogo, "company-logos");
                }

                company = Company.builder()
                        .title(companyTitle)
                        .description(companyDescription)
                        .location(companyLocation)
                        .logo(logoFilename)
                        .build();

                company = companyRepository.save(company);
            }
        }

        // Handle job image upload
        String imageFilename = null;
        if (image != null && !image.isEmpty()) {
            imageFilename = saveFile(image, "job-images");
        }

        Job job = Job.builder()
                .title(title)
                .description(description)
                .category(category)
                .image(imageFilename)
                .company(company)
                .build();

        return jobRepository.save(job);
    }


    private String saveFile(MultipartFile file, String folder) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String imageName = UUID.randomUUID() + "_" + originalFilename;

        // ✅ Use absolute path
        String uploadFolder = System.getProperty("user.dir") + "/uploads/" + folder;
        Path uploadDir = Paths.get(uploadFolder);

        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        // ✅ Save file to absolute path
        Files.copy(file.getInputStream(),
        uploadDir.resolve(imageName),
        StandardCopyOption.REPLACE_EXISTING);

        return imageName;
    }
}
