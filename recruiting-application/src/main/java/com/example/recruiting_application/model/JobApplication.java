package com.example.recruiting_application.model;

import jakarta.persistence.*;

@Entity
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long jobId;
    private Long userId;
    private String resumeFileName;

    @Lob
    private byte[] resumeData;




    public byte[] getResumeData() {
        return resumeData;
    }

    public void setResumeData(byte[] resumeData) {
        this.resumeData = resumeData;
    }

    public String getResumeFileName() {
        return resumeFileName;
    }

    public void setResumeFileName(String resumeFileName) {
        this.resumeFileName = resumeFileName;
    }

    public JobApplication() {
    }
    public JobApplication(Long jobId, Long userId) {
        this.jobId = jobId;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


}
