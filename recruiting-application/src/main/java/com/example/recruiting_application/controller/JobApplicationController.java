package com.example.recruiting_application.controller;

import com.example.recruiting_application.dto.JobApplicationDTO;
import com.example.recruiting_application.model.JobApplication;
import com.example.recruiting_application.service.EmailService;
import com.example.recruiting_application.service.JobApplicationService;
import com.example.recruiting_application.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class JobApplicationController {
    private JobApplicationService jobApplicationService;
    private EmailService emailService;
    private UserService userService;
    public JobApplicationController(JobApplicationService jobApplicationService, EmailService emailService, UserService userService){
        this.jobApplicationService = jobApplicationService;
        this.emailService = emailService;
        this.userService = userService;
    }

    // to apply for the jobs



    @PostMapping("/apply")
    public ResponseEntity<JobApplication> applyForJob(
            @RequestParam("userId") Long userId,
            @RequestParam("jobId") Long jobId,
            @RequestParam("resume") MultipartFile resumeFile) {
        try {
            // Convert the uploaded file to a byte array
            byte[] resumeData = resumeFile.getBytes();

            JobApplication jobApplication = new JobApplication();
            jobApplication.setJobId(jobId);
            jobApplication.setUserId(userId);
            jobApplication.setResumeFileName(resumeFile.getOriginalFilename()); // Store filename
            jobApplication.setResumeData(resumeData); // Store the file data

            JobApplication savedApplication = jobApplicationService.applyForJob(jobApplication);



            //fetch user email
            String userEmail = getEmailById(userId);
            if(userEmail != null){
                emailService.sendApplicationSubmissionEmail(userEmail);
            }
            return new ResponseEntity<>(savedApplication, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getEmailById(Long userId) {
        return userService.getUserEmailById(userId);
    }

    @GetMapping("/{id}/resume")
    public ResponseEntity<byte[]> getResume(@PathVariable Long id) {
        JobApplication application = jobApplicationService.getApplicationById(id);
        if (application != null && application.getResumeData() != null) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + application.getResumeFileName() + "\"")
                    .body(application.getResumeData());
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    //to save the resume

    private String saveResumeFile(MultipartFile resumeFile) throws IOException {
        // Define a directory to save resumes (can be any path you choose)
        String uploadDir = "uploads/resumes/";
        String fileName = resumeFile.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);

        // Save the file to the defined path
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, resumeFile.getBytes());

        return fileName; // Return the file name for storage in the database
    }

    // to show those who are applied for the jobs

    @GetMapping("/all")
    public List<JobApplicationDTO> getAppliedJobs(){
        List<JobApplicationDTO> applicationDTOS = jobApplicationService.getAllApplications();
        System.out.println("Fetched Applications: " + applicationDTOS);
        return applicationDTOS;
    }
}
