package com.example.recruiting_application.dto;

public class JobApplicationDTO {
    private Long applicationId;
    private Long jobId;
    private String jobTitle; // from Jobs table
    private Long userId;
    private String userEmail; // from User table
    private Long userMobileNumber; // from User table
    private String resumeFileName;

    public JobApplicationDTO(Long applicationId, Long jobId, String jobTitle, Long userId, String userEmail, Long userMobileNumber, String resumeFileName) {
        this.applicationId = applicationId;
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.userId = userId;
        this.userEmail = userEmail;
        this.userMobileNumber = userMobileNumber;
        this.resumeFileName = resumeFileName;
    }



    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Long getUserMobileNumber() {
        return userMobileNumber;
    }

    public void setUserMobileNumber(Long userMobileNumber) {
        this.userMobileNumber = userMobileNumber;
    }

    public String getResumeFileName() {
        return resumeFileName;
    }

    public void setResumeFileName(String resumeFileName) {
        this.resumeFileName = resumeFileName;
    }
}
