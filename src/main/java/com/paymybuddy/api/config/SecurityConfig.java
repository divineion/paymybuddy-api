package com.paymybuddy.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration // indiquera à Spring qu'il y a des beans dans la classe
@EnableWebSecurity //permet de configurer des éléments de sécurity
public class SecurityConfig {
	// permettre de modéliser une chaîne de filtres de sécurité
	@Bean // enregistre la valeur de retour en tant que Bean 
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
                .csrf(csrf -> csrf.disable())
                // méthode  requestMatchers()  pour définir l'association des rôles  USER  (utilisateur) et ADMIN  (administrateur) avec des pages
                .authorizeHttpRequests(auth -> {
                    auth.anyRequest().authenticated();
                })
                .formLogin(Customizer.withDefaults())
                .build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
}
