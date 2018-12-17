package com.tib.oauth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.web.cors.CorsConfiguration;

@EnableResourceServer
@Configuration
//@Profile("mvc")
// This isn't the main/standard Resource Server of the project (that's in a different module)
// This is the Resource Server for the Testing OAuth2 with Spring MVC article: http://www.baeldung.com/oauth-api-testing-with-spring-mvc
// Notice that it's only active via the mvc profile
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {

        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        http.cors().configurationSource(request -> corsConfiguration)
            .and()
            .csrf().disable()
            .authorizeRequests().antMatchers("/v1/api/").hasAnyRole();

    }

    @Primary
    @Bean
    public RemoteTokenServices tokenService() {
        RemoteTokenServices tokenService = new RemoteTokenServices();
        tokenService.setCheckTokenEndpointUrl(
            "http://localhost:8090/example-oauth-server/oauth/check_token");
        tokenService.setClientId("example_client_id");
        tokenService.setClientSecret("secret");
        return tokenService;
    }

}
