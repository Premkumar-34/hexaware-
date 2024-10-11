package com.example.recruiting_application.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "forget_password")
public class ForgetPassword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long forgetPasswordId;
    @Column(nullable = false)
    private Integer otp;
    @Column(nullable = false)
    private Date expirationTime;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Long getForgetPasswordId() {
        return forgetPasswordId;
    }

    public void setForgetPasswordId(Long forgetPasswordId) {
        this.forgetPasswordId = forgetPasswordId;
    }

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
