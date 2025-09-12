package com.syxbruno.authentication_project.email.user;

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
  private final String SOURCE_EMAIL = "authenticationproject@gmail.com";
  private final String SENDER_NAME = "authentication project by syxbruno";
  private final String URL_WEBSITE = "http://localhost:8080";

  public void sendEmailResetPassword(User user) {

    String subject = "Email to change password";

    String content = generateContent(
        "How are you, [[name]]? <br>"
            + "This is your endpoint to change/reset your password: <br>"
            + " <h3>[[url]]</h3>"
            + " Authentication Project", user.getName(), URL_WEBSITE + "/send-password?code=" + user.getToken()
    );
  }

  private String generateContent(String template, String name, String url) {

    return template.replace("[[name]]", name).replace("[[url]]", url);
  }

  @Async
  private void sendEmail(String emailUser, String subject, String content) {

    MimeMessage mimeMessage = sender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

    try {

      helper.setFrom(SOURCE_EMAIL, SENDER_NAME);
      helper.setTo(emailUser);
      helper.setSubject(subject);
      helper.setText(content, true);

    } catch (MessagingException | UnsupportedEncodingException e) {

      throw new BusinessRules("");
    }
  }
}
