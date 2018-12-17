package com.tib.oauth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServerConfigInMemory extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private Environment env;

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(final AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {// @formatter:off
        clients.inMemory()
            .withClient("example_client_id")
            .authorizedGrantTypes("authorization_code", "refresh_token")
            .redirectUris("http://localhost:8091/example-resource-server/v1/auth/authenticated")
            .redirectUris("https://www.getpostman.com/oauth2/callback","http://localhost:8091/example-resource-server")
            .scopes("read", "write", "foo", "bar")
            .accessTokenValiditySeconds(3600) // 1 hour
            .secret(passwordEncoder().encode("secret"))
            .refreshTokenValiditySeconds(2592000) // 30 days

            .and()
            .withClient("example_client_id_short")
            .authorizedGrantTypes("authorization_code", "refresh_token")
            .redirectUris("https://www.getpostman.com/oauth2/callback")
            .scopes("read", "write", "foo", "bar")
            .accessTokenValiditySeconds(60) // 1 hour
            .secret(passwordEncoder().encode("secret"))
            .refreshTokenValiditySeconds(2592000) // 30 days

            .and()
            .withClient("postman_client_id")
            .secret("secret")
            .redirectUris("https://www.getpostman.com/oauth2/callback")
            .authorizedGrantTypes("authorization_code", "refresh_token")
            .scopes("foo", "read", "write")
            .secret(passwordEncoder().encode("secret"))
            .accessTokenValiditySeconds(3600) // 1 hour
            .refreshTokenValiditySeconds(2592000); // 30 days

    }

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer()));
		endpoints.tokenStore(tokenStore())
				// .accessTokenConverter(accessTokenConverter())
				.tokenEnhancer(tokenEnhancerChain).authenticationManager(authenticationManager);
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }


    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }
}
