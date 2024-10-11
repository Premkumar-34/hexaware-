package com.example.recruiting_application.service;

import com.example.recruiting_application.dto.EmailDetails;

public interface EmailService {
    String simpleMail(EmailDetails emailDetails);

    String sendApplicationSubmissionEmail(String recipient);

}
