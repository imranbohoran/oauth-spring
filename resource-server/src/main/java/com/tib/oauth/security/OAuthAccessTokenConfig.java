package com.tib.oauth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import java.util.Arrays;

@EnableOAuth2Client
@Configuration
public class OAuthAccessTokenConfig {

    private final Environment environment;

    @Autowired
    public OAuthAccessTokenConfig(Environment environment) {this.environment = environment;}

    @Bean
    protected OAuth2ProtectedResourceDetails resource() {

        String authUri = environment.getProperty("security.oauth2.client.userAuthorizationUri");
        String accessTokenURI = environment.getProperty("security.oauth2.client.accessTokenUri");
        String clientId = environment.getProperty("security.oauth2.client.clientId");
        String clientSecret = environment.getProperty("security.oauth2.client.clientSecret");

        AuthorizationCodeResourceDetails authorizationCodeResourceDetails = new AuthorizationCodeResourceDetails();

        authorizationCodeResourceDetails.setAccessTokenUri(accessTokenURI);
        authorizationCodeResourceDetails.setUserAuthorizationUri(authUri);
        authorizationCodeResourceDetails.setClientId(clientId);
        authorizationCodeResourceDetails.setClientSecret(clientSecret);
        authorizationCodeResourceDetails.setClientAuthenticationScheme(AuthenticationScheme.form);
        authorizationCodeResourceDetails.setAuthenticationScheme(AuthenticationScheme.query);

        return authorizationCodeResourceDetails;
    }

    @Bean
    public OAuth2RestOperations restTemplate() {
        AccessTokenRequest atr = new DefaultAccessTokenRequest();

        return new OAuth2RestTemplate(resource(), new DefaultOAuth2ClientContext(atr));
    }
}
