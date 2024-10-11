package com.example.recruiting_application.service;



import com.example.recruiting_application.dto.EmailDetails;
import com.example.recruiting_application.model.User;

import com.example.recruiting_application.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepo repo;
    private final PasswordEncoder passwordEncoder;
    private EmailServiceImpl emailServiceImpl;



    @Autowired
    public UserService(UserRepo repo, PasswordEncoder passwordEncoder, EmailServiceImpl emailService) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
        this.emailServiceImpl = emailService;

    }

    public User registerUser(User user) {
        if(repo.findByEmail(user.getEmail()) != null){
            throw new IllegalArgumentException("Email is already registered.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User newUser = repo.save(user);
        sendRegistrationSuccessEmail(newUser);
        return newUser;
    }

    private void sendRegistrationSuccessEmail(User user) {
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setRecipient(user.getEmail());
        emailDetails.setSubject("Registration Successful");
        emailDetails.setMsgBody("Hello " + user.getFullName() + ",\n\nThank you for registering at ByteMinds. Your account has been successfully created.\n\nBest Regards,\nByteMinds Team");

        emailServiceImpl.sendSimpleMail(emailDetails);  // Assuming this is how you send emails
    }

    public User findUserByEmail(String email) {
        System.out.println("Searching for " + email);
        return repo.findByEmail(email);
    }

    public boolean validateUser(String email, String password) {
        User user = repo.findByEmail(email);
        if (user != null) {
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repo.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }


    public Long getUserIdByEmail(String email) {
        User user = repo.findByEmail(email);
        if(user != null){
            return user.getId();
        }
        return (long) -1;
    }

    public String getUserEmailById(Long userId) {
        User user = repo.findById(userId).orElse(null);
        return user != null ? user.getEmail() : null;
    }

    public boolean updateUser(Long id, User updatedUser) {
        Optional<User> existingUserOptimal = repo.findById(id);
        if(existingUserOptimal.isPresent()){
            User existingUser = existingUserOptimal.get();
            existingUser.setFullName(updatedUser.getFullName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setMobileNumber(updatedUser.getMobileNumber());
            repo.save(existingUser);
            return true;
        }
        return false;
    }
}