package com.syxbruno.authentication_project.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "google.oauth.client")
public class GoogleOAuthProperties {

  private String id;
  private String secret;
}
