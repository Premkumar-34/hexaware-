package com.example.recruiting_application.repository;

import com.example.recruiting_application.model.ForgetPassword;
import com.example.recruiting_application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForgetPassRepo extends JpaRepository<ForgetPassword, Long> {

    Optional<ForgetPassword> findByUser(User user);
}
