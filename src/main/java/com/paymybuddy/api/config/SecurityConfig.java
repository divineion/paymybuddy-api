package com.paymybuddy.api.config;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.jwk.RSAKey;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@Configuration // indiquera à Spring qu'il y a des beans dans la classe
@EnableWebSecurity //permet de configurer des éléments de sécurity
public class SecurityConfig {
	// permettre de modéliser une chaîne de filtres de sécurité
	@Bean // enregistre la valeur de retour en tant que Bean 
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
                .csrf(csrf -> csrf.disable())
                // STATELESS pour ne pas créer de session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))        
        		// méthode  requestMatchers()  pour définir l'association des rôles  USER  (utilisateur) et ADMIN  (administrateur) avec des pages
                .authorizeHttpRequests(auth -> {
                	auth.requestMatchers("/login_check").permitAll();
                    auth.requestMatchers("/api/**").authenticated();
                })
                // activer OAuth2 et le support jwt
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults())) 
                .build();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	//cofngiruer l'encoder et le decoder
	//injecter les clés à partir de RsaKeyConfig
	// injecter la clé publique retournée par la méthode bean déclarée dans RsaKeyConfig
	@Bean
	JwtDecoder jwtDecoder(RSAPublicKey publicKey) {
		//Use the given public key to validate JWTs
		return NimbusJwtDecoder.withPublicKey(publicKey).build();
	}
	
	// https://docs.spring.io/spring-security/reference/api/java/org/springframework/security/oauth2/jwt/NimbusJwtEncoder.html
	@Bean
	JwtEncoder jwtEncoder(RSAPublicKey publickey, RSAPrivateKey privateKey) {
		// créer une json web key avec clé publique + privée
		RSAKey jwk = new RSAKey.Builder(publickey)
				.privateKey(privateKey)
				.keyUse(KeyUse.SIGNATURE)
				.keyID(ConfigConstants.RSA_KEY_ID)
				.build();
		JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
		
		return new NimbusJwtEncoder(jwks);
	}
}
