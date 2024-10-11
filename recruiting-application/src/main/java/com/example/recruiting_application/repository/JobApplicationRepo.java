package com.example.recruiting_application.repository;

import com.example.recruiting_application.dto.JobApplicationDTO;
import com.example.recruiting_application.model.JobApplication;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplicationRepo extends JpaRepository<JobApplication, Long> {
    @Query("SELECT new com.example.recruiting_application.dto.JobApplicationDTO(ja.id, ja.jobId, j.jobTitle, ja.userId, u.email, u.mobileNumber, ja.resumeFileName) " +
            "FROM JobApplication ja " +
            "JOIN User u ON ja.userId = u.id " +
            "JOIN Jobs j ON ja.jobId = j.id")
    List<JobApplicationDTO> findAllApplicationsWithUserDetails();

//    @Query("SELECT ja FROM JobApplication ja JOIN Job j ON ja.jobId = j.id WHERE ja.userId = :userId AND ja.jobId = :jobId")
//    JobApplication findByUserIdAndJobId(@Param("userId") Long userId, @Param("jobId") Long jobId);


}

