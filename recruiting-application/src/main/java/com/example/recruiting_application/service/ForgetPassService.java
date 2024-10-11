package com.example.recruiting_application.service;

import com.example.recruiting_application.dto.EmailDetails;
import com.example.recruiting_application.model.ForgetPassword;
import com.example.recruiting_application.model.User;
import com.example.recruiting_application.repository.ForgetPassRepo;
import com.example.recruiting_application.repository.UserRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
public class ForgetPassService {
    private UserRepo userRepo;
    private ForgetPassRepo forgetPassRepo;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private EmailService emailService;

    public ForgetPassService(UserRepo userRepo, ForgetPassRepo forgetPassRepo, EmailService emailService){
        this.forgetPassRepo = forgetPassRepo;
        this.userRepo = userRepo;
        this.emailService = emailService;
    }

    // to generate the opt and save it in the database
    public boolean generateOtp(String email){
        Optional<User> userOptional = Optional.ofNullable(userRepo.findByEmail(email));
        if(userOptional.isPresent()){
            User user = userOptional.get();

            ForgetPassword forgetPassword = new ForgetPassword();
            Integer otp = generateRandomOtp();
            forgetPassword.setUser(user);
            forgetPassword.setOtp(otp);
            forgetPassword.setExpirationTime(new Date(System.currentTimeMillis() + 10 * 60 * 1000));

            forgetPassRepo.save(forgetPassword);


            //email details to send
            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setRecipient(email);
            emailDetails.setSubject("Your OTP Code");
            emailDetails.setMsgBody("Your OTP Code is: " + otp);

            String emailResponse = emailService.simpleMail(emailDetails);
            if(emailResponse.contains("Error")){
                return false;
            }
            return true;

        }
        return false;
    }

    //to verify the otp and reset the password

    public boolean verifyOtpAndResetPassword(String email, int otp, String newPassword){
        Optional<User> userOptional = Optional.ofNullable(userRepo.findByEmail(email));
        if(userOptional.isPresent()){
            User user = userOptional.get();
            Optional<ForgetPassword> forgetPasswordOptional = forgetPassRepo.findByUser(user);
            if(forgetPasswordOptional.isPresent()){
                ForgetPassword forgetPassword = forgetPasswordOptional.get();
                if(forgetPassword.getOtp().equals(otp) && forgetPassword.getExpirationTime().after(new Date())){

                    user.setPassword(passwordEncoder.encode(newPassword));
                    userRepo.save(user);
                    forgetPassRepo.delete(forgetPassword);
                    return true;
                }
            }
        }
        return false;
    }

    // to generate the random 6 digit number
    private Integer generateRandomOtp() {
        Random random = new Random();
        return 100000 + random.nextInt(900000);
    }


}
