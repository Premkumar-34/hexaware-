package com.example.recruiting_application.repository;

import com.example.recruiting_application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {


    User findByEmail(String email);
//    User findByEmail(String username);


}
