package com.example.jwtresourceclientservlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;

@SpringBootApplication
public class JwtresourceclientservletApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(JwtresourceclientservletApplication.class, args);
    }

    @Autowired
    private WebClient webClient;
    @Override
    public void run(ApplicationArguments args) throws Exception {

        System.out.println("r = ");
        String r = webClient.get()
                .uri("http://localhost:8080/api/hello")
                .attributes(clientRegistrationId("auth0"))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        System.out.println("r = " + r);
    }
}
