package com.weatherforcast.api.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.weatherapi.forecast.common.ClientApp;
import com.weatherforcast.api.clientapp.ClientAppRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Optional;

@Configuration

public class AuthorizationServerConfig {
    private final RsaKeyProperties rsakeys;

    @Value("${app.security.jwt.issuer}")
    private String issuerName;

    @Value("${app.security.jwt.access-token.expiration}")
    private int accessTokenExpirationTime;



    public AuthorizationServerConfig(RsaKeyProperties rsakeys) {
        this.rsakeys = rsakeys;
    }


    @Bean
    JwtDecoder jwtDecoder(){
        return NimbusJwtDecoder.withPublicKey(rsakeys.publicKey()).build();
    }

    @Bean
    JwtEncoder jwtEncoder(){
        JWK jwk= new RSAKey.Builder(rsakeys.publicKey()).privateKey(rsakeys.privateKey()).build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));

        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws  Exception{
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity);
        return httpSecurity.build();
    }


    @Bean
    RegisteredClientRepository registeredClientRepository(ClientAppRepository clientAppRepository){
        return  new RegisteredClientRepository() {
            @Override
            public void save(RegisteredClient registeredClient) {

            }

            @Override
            public RegisteredClient findById(String id) {
                return null;
            }

            @Override
            public RegisteredClient findByClientId(String clientId) {
                Optional<ClientApp> result = clientAppRepository.findByClientId(clientId);

                if(result.isEmpty())
                    return null;
                ClientApp clientApp = result.get();

                return RegisteredClient.withId(clientApp.getId().toString())
                        .clientName(clientApp.getName())
                        .clientId(clientApp.getClientId())
                        .clientSecret(clientApp.getClientSecret())
                        .scope(clientApp.getRole().toString())
                        .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                        .build();
            }
        };
    }
}
