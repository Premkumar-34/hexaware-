package com.example.recruiting_application.controller;

import com.example.recruiting_application.dto.JobApplicationDTO;
import com.example.recruiting_application.model.JobApplication;
import com.example.recruiting_application.service.EmailService;
import com.example.recruiting_application.service.JobApplicationService;
import com.example.recruiting_application.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
public class JobApplicationController {
    private JobApplicationService jobApplicationService;
    private EmailService emailService;
    private UserService userService;
    private final RestTemplate restTemplate;
    public JobApplicationController(JobApplicationService jobApplicationService, EmailService emailService, UserService userService, RestTemplate restTemplate){
        this.jobApplicationService = jobApplicationService;
        this.emailService = emailService;
        this.userService = userService;
        this.restTemplate =restTemplate;
    }

    // to apply for the jobs



//    @PostMapping("/apply")
//    public ResponseEntity<JobApplication> applyForJob(
//            @RequestParam("userId") Long userId,
//            @RequestParam("jobId") Long jobId,
//            @RequestParam("resume") MultipartFile resumeFile,
//            @RequestParam("aiScore") Double aiScore) {
//        try {
//            // Convert the uploaded file to a byte array
//            byte[] resumeData = resumeFile.getBytes();
//
//            JobApplication jobApplication = new JobApplication();
//            jobApplication.setJobId(jobId);
//            jobApplication.setUserId(userId);
//            jobApplication.setResumeFileName(resumeFile.getOriginalFilename());// Store filename
//
//            jobApplication.setResumeData(resumeData); // Store the file data
//            jobApplication.setAiScore(aiScore);
//
//            JobApplication savedApplication = jobApplicationService.applyForJob(jobApplication);
//
//
//
//            //fetch user email
//            String userEmail = getEmailById(userId);
//            if(userEmail != null){
//                emailService.sendApplicationSubmissionEmail(userEmail);
//            }
//            return new ResponseEntity<>(savedApplication, HttpStatus.CREATED);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }



    @PostMapping("/apply")
    public ResponseEntity<JobApplication> applyForJob(
            @RequestParam("userId") Long userId,
            @RequestParam("jobId") Long jobId,
            @RequestParam("resume") MultipartFile resumeFile) {
        Logger logger = LoggerFactory.getLogger(JobApplicationController.class);

        // Enhanced logging
        logger.info("Received application for userId: {}, jobId: {}, resumeFileSize: {}",
                userId, jobId, resumeFile.getSize());

        try {
            // Read the resume file into a byte array
            byte[] resumeData = resumeFile.getBytes();

            // Prepare request to the AI service
            String aiUrl = "http://127.0.0.1:5000/api/ai-process";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // Prepare the file to be sent
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("resume", new ByteArrayResource(resumeData) {
                @Override
                public String getFilename() {
                    return resumeFile.getOriginalFilename(); // Provide the original filename
                }
            });

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // Call the AI service to get the score
            ResponseEntity<Map> aiResponse;
            try {
                aiResponse = restTemplate.postForEntity(aiUrl, requestEntity, Map.class);
                logger.info("AI service response: {}", aiResponse.getBody()); // Log the AI service response
            } catch (RestClientException e) {
                logger.error("Error calling AI service: {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new JobApplication()); // Return an empty JobApplication instead
            }

            // Extract the AI score from the response
            Double aiScore = (Double) aiResponse.getBody().get("aiScore"); // Update the key here
            logger.info("AI score: {}", aiScore); // Log the AI score

            if (aiScore == null) {
                logger.error("AI score is null.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new JobApplication()); // Return an empty JobApplication instead
            }

            // Create and save the job application
            JobApplication jobApplication = new JobApplication();
            jobApplication.setJobId(jobId);
            jobApplication.setUserId(userId);
            jobApplication.setResumeFileName(resumeFile.getOriginalFilename());
            jobApplication.setResumeData(resumeData); // Store the file data
            jobApplication.setAiScore(aiScore);

            JobApplication savedApplication = jobApplicationService.applyForJob(jobApplication);

            // Fetch user email and send notification
            String userEmail = getEmailById(userId);
            if (userEmail != null) {
                emailService.sendApplicationSubmissionEmail(userEmail);
            }

            logger.info("Application submitted successfully for userId: {}", userId);
            return new ResponseEntity<>(savedApplication, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error processing application: {}", e.getMessage());
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
