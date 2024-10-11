package com.example.recruiting_application.service;

import com.example.recruiting_application.dto.JobApplicationDTO;
import com.example.recruiting_application.model.JobApplication;
import com.example.recruiting_application.repository.JobApplicationRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class JobApplicationService {
    private JobApplicationRepo jobApplicationRepo;

    public JobApplicationService(JobApplicationRepo jobApplicationRepo){
        this.jobApplicationRepo = jobApplicationRepo;
    }

    public JobApplication applyForJob(JobApplication jobApplication){
        return jobApplicationRepo.save(jobApplication);
    }


    public List<JobApplicationDTO> getAllApplications() {
        return jobApplicationRepo.findAllApplicationsWithUserDetails();
    }
    public JobApplication getApplicationById(Long id) {
        Optional<JobApplication> optionalApplication = jobApplicationRepo.findById(id);
        return optionalApplication.orElse(null); // Return the application if found, else null
    }



}
