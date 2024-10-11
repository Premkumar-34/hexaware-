package com.example.recruiting_application.service;

import com.example.recruiting_application.model.Jobs;
import com.example.recruiting_application.repository.JobRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {
    private JobRepo jobRepo;
    public JobService(JobRepo jobRepo){
        this.jobRepo = jobRepo;
    }


    public Jobs findById(Long id){
        return jobRepo.findById(id).orElse(null);
    }

    public Jobs saveJob(Jobs jobs) {
        return jobRepo.save(jobs);
    }


    public List<Jobs> alljobs() {
        return jobRepo.findAll();
    }


    public List<Jobs> getDrafts(){
        return jobRepo.findByStatus("draft");
    }
    public Jobs updateJob(Long id, Jobs updatedJob) {
        Optional<Jobs> existingJob = jobRepo.findById(id);
        if (existingJob.isPresent()) {
            Jobs job = existingJob.get();
            job.setJobTitle(updatedJob.getJobTitle());
            job.setJobDescription(updatedJob.getJobDescription());
            job.setDepartment(updatedJob.getDepartment());
            job.setJobLocation(updatedJob.getJobLocation());
            job.setEmploymentType(updatedJob.getEmploymentType());
            job.setSalaryRange(updatedJob.getSalaryRange());
            job.setApplicationDeadline(updatedJob.getApplicationDeadline());
            job.setRequiredQualifications(updatedJob.getRequiredQualifications());
            job.setPreferredQualifications(updatedJob.getPreferredQualifications());
            job.setResponsibilities(updatedJob.getResponsibilities());
            job.setStatus(updatedJob.getStatus());  // Update status, if applicable

            return jobRepo.save(job);  // Save the updated job
        } else {
            throw new RuntimeException("Job not found with id " + id);
        }
    }

    public List<Jobs> getPublished() {
        return jobRepo.findByStatus("publish");
    }

    public boolean deleteJob(Long id) {
        Optional<Jobs> jobs =jobRepo.findById(id);
        if (jobs.isPresent()){
            jobRepo.deleteById(id);
            return true;
        }else {
            return false;
        }
    }
}
