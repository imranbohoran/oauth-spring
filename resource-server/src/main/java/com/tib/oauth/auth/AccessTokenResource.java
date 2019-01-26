package com.tib.oauth.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@RestController
public class AccessTokenResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessTokenResource.class);

    private final OAuth2RestOperations oauthOperations;
    private final Environment environment;
    private final RestTemplateBuilder restTemplateBuilder;

    @Autowired
    public AccessTokenResource(OAuth2RestOperations oauthOperations, Environment environment,RestTemplateBuilder restTemplateBuilder) {
        this.oauthOperations = oauthOperations;
        this.environment = environment;
        this.restTemplateBuilder = restTemplateBuilder;
    }

    @GetMapping(path = "/v1/auth/access_token")
    public String generateAccessToke(@RequestParam(value = "existing_code", required = false) String code) {

        return oauthOperations.getAccessToken().getValue();
    }

    @GetMapping(path = "/v1/auth/authenticated")
    public void authenticatedUser(HttpServletResponse httpServletResponse,
                                  @RequestParam(value = "code", required = false) String code,
                                  @RequestParam(value = "client_redirect_uri") String clientRedirect) {
        LOGGER.info("User authenticated with code: "+ code);
        LOGGER.info("Client redirect URI "+ clientRedirect);

        String rawToken = swapCodeForToken(code, clientRedirect);

        httpServletResponse.setHeader("Location", clientRedirect + "?code="+rawToken);
        httpServletResponse.setStatus(302);
    }

    private String swapCodeForToken(String code, String redirectUri) {
        String accessToken = "not-set";

        String tokenUri = environment.getProperty("security.oauth2.client.accessTokenUri");
        String clientId = environment.getProperty("security.oauth2.client.clientId");
        String secret = environment.getProperty("security.oauth2.client.clientSecret");

        MultiValueMap<String, String> requestData = new LinkedMultiValueMap<>();
        requestData.add("grant_type", "authorization_code");
        requestData.add("code", code);
        requestData.add("redirect_uri", "http://localhost:8091/example-resource-server/v1/auth/authenticated?client_redirect_uri="+redirectUri);

        RestTemplate restTemplate = restTemplateBuilder.basicAuthorization(clientId, secret)
            .build();

        Map map = restTemplate.postForObject(tokenUri, requestData, Map.class);

        if (map != null) {
            accessToken = map.get("access_token").toString();
        }
        return accessToken;
    }

    private HttpHeaders createHeaders(String username, String password){
        return new HttpHeaders() {{
            String credentials = username + ":" + password;
            String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
            String authHeader = "Basic " + encodedCredentials;
            set( "Authorization", authHeader );
        }};
    }

    @GetMapping(path = "/v1/sample/app-redirect")
    public String appReidirectWithAccessCode(@RequestParam(value = "code") String code) {
        LOGGER.info("Received access token: "+ code);
        return "obtained toke: "+ code;
    }
}
