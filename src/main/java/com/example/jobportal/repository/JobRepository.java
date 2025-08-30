package com.example.jobportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jobportal.model.Job;

public interface JobRepository extends JpaRepository<Job, Long> {}
