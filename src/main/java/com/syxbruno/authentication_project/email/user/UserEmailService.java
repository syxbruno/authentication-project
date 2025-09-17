package com.syxbruno.authentication_project.email.user;

import com.syxbruno.authentication_project.config.properties.EmailProperties;
import com.syxbruno.authentication_project.exception.BusinessRules;
import com.syxbruno.authentication_project.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEmailService {

  private final JavaMailSender sender;
  private final EmailProperties properties;
  private final String SENDER_NAME = "authentication project by bruno";

  public void sendEmailResetPassword(User user) {

    String subject = "Email to change password";

    String content = generateContent(
        "Hello [[name]], how are you? <br>"
            + "This is your endpoint and your token to change/reset your password: <br>"
            + " <h3>Endpoint: [[url]]  Token: [[token]]</h3>"
            + " Authentication Project", user.getName(), user.getToken()
    );
  }

  private String generateContent(String template, String name, String token) {

    return template.replace("[[name]]", name).replace("[[url]]", "/alter-password").replace("[[token]]", token);
  }

  @Async
  private void sendEmail(String emailUser, String subject, String content) {

    MimeMessage mimeMessage = sender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

    try {

      helper.setFrom(properties.getUsername(), SENDER_NAME);
      helper.setTo(emailUser);
      helper.setSubject(subject);
      helper.setText(content, true);

    } catch (MessagingException | UnsupportedEncodingException e) {

      throw new BusinessRules("Error sending email");
    }
  }
}