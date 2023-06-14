package com.example.jwtresourceclientservlet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.security.oauth2.core.AuthorizationGrantType.CLIENT_CREDENTIALS;

@Configuration
public class SecurityConfig {
    private final Auth0 auth0;

    public SecurityConfig(Auth0 auth0) {
        this.auth0 = auth0;
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationsRepository() {
        ClientRegistration registration = ClientRegistration
                .withRegistrationId("auth0")
                .tokenUri(auth0.getTokenUri())
                .clientId(auth0.getClientId())
                .clientSecret(auth0.getClientSecret())
                .authorizationGrantType(CLIENT_CREDENTIALS)
                .build();
        return new InMemoryClientRegistrationRepository(registration);
    }

    @Bean
    public OAuth2AuthorizedClientService oAuth2AuthorizedClientService(ClientRegistrationRepository clientRegistrationsRepository) {
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationsRepository);
    }

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientService authorizedClientService) {
        OAuth2AuthorizedClientProvider authorizedClientProvider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .refreshToken()
                        .clientCredentials()
                        .build();

        AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                        clientRegistrationRepository, authorizedClientService);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);


        return authorizedClientManager;
    }

    @Bean
    WebClient webClient(OAuth2AuthorizedClientManager authorizedClientManager) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2Client =
                new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        oauth2Client.setDefaultOAuth2AuthorizedClient(true);
        return WebClient.builder()
                .apply(oauth2Client.oauth2Configuration())
                .build();
    }

}
