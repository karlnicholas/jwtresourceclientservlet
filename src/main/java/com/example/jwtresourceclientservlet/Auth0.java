package com.example.jwtresourceclientservlet;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties
@PropertySource("classpath:auth0.properties")
@Data
public class Auth0 {
    private String tokenUri;
    private String clientId;
    private String clientSecret;
}
