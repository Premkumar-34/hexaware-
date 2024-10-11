package com.example.recruiting_application.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Data
public class Jobs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String jobTitle;
    @Column(length = 500)
    private String jobDescription;
    private String department;
    private String jobLocation;
    private String employmentType;
    private Double salaryRange;
    private LocalDate applicationDeadline;
    private String requiredQualifications;
    private String preferredQualifications;
    private String responsibilities;
    private String status; // Either 'draft' or 'publish'

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public Double getSalaryRange() {
        return salaryRange;
    }

    public void setSalaryRange(Double salaryRange) {
        this.salaryRange = salaryRange;
    }

    public LocalDate getApplicationDeadline() {
        return applicationDeadline;
    }

    public void setApplicationDeadline(LocalDate applicationDeadline) {
        this.applicationDeadline = applicationDeadline;
    }

    public String getRequiredQualifications() {
        return requiredQualifications;
    }

    public void setRequiredQualifications(String requiredQualifications) {
        this.requiredQualifications = requiredQualifications;
    }

    public String getPreferredQualifications() {
        return preferredQualifications;
    }

    public void setPreferredQualifications(String preferredQualifications) {
        this.preferredQualifications = preferredQualifications;
    }

    public String getResponsibilities() {
        return responsibilities;
    }

    public void setResponsibilities(String responsibilities) {
        this.responsibilities = responsibilities;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
