package com.example.recruiting_application.repository;

import com.example.recruiting_application.model.Jobs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepo extends JpaRepository<Jobs, Long > {

    List<Jobs> findByJobTitleContainingIgnoreCaseAndStatus(String jobTitle, String status);
    List<Jobs> findByStatus(String status);


}
