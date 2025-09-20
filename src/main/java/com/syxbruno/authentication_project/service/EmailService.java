package com.syxbruno.authentication_project.service;

import com.syxbruno.authentication_project.config.properties.EmailProperties;
import com.syxbruno.authentication_project.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender sender;
  private final EmailProperties properties;

  public String sendEmailResetPassword(User user) {

    String subject = "Email to change password";

    String content = generateContent(
        "Hello [[name]], how are you? <br>"
            + "This is your endpoint and your token to change/reset your password: <br>"
            + " <h3>Endpoint: [[url]]  Token: [[token]]</h3>"
            + " Authentication Project", user.getName(), "/alter-password", user.getToken()
    );

    return sendEmail(user.getEmail(), subject, content);
  }

  public String sendEmailVerifyUser(User user) {

    String subject = "Account verification email";

    String content = generateContent(
        "Hello [[name]], how are you? <br>"
            + "This is your endpoint and your token to verify your email: <br>"
            + " <h3>Endpoint: [[url]]  Token: [[token]]</h3>"
            + " Authentication Project", user.getName(), "/verify", user.getToken()
    );

    return sendEmail(user.getEmail(), subject, content);
  }

  @Async
  private String sendEmail(String emailUser, String subject, String content) {

    try {
      MimeMessage message = sender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

      helper.setFrom(properties.getUsername());
      helper.setTo(emailUser);
      helper.setSubject(subject);
      helper.setText(content, true);

      sender.send(message);
      return "Email sent successfully, check your mailbox";

    } catch (MessagingException e) {
      return "Error sending email: " + e.getLocalizedMessage();
    }
  }

  private String generateContent(String template, String name, String url, String token) {

    return template.replace("[[name]]", name).replace("[[url]]", url).replace("[[token]]", token);
  }
}