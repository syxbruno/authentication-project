package com.syxbruno.authentication_project.service;

import com.atlassian.onetime.core.TOTPGenerator;
import com.atlassian.onetime.model.TOTPSecret;
import com.atlassian.onetime.service.RandomSecretProvider;
import com.syxbruno.authentication_project.model.User;
import org.springframework.stereotype.Service;

@Service
public class TotpService {

  public String generateSecret() {

    return new RandomSecretProvider().generateSecret().getBase32Encoded();
  }

  public String generateQrCode(User user) {

    String issuer = "authentication-project";
    return String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", issuer, user.getUsername(), user.getSecret(), issuer);
  }

  public Boolean verifyCode(String code, User user) {

    TOTPSecret secret = TOTPSecret.Companion.fromBase32EncodedString(user.getSecret());
    String codeApp = new TOTPGenerator().generateCurrent(secret).getValue();

    return codeApp.equals(code);
  }
}
