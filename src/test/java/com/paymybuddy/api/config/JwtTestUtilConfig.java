package com.paymybuddy.api.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;


@TestConfiguration
public class JwtTestUtilConfig {
    @Bean
    JwtTestUtil jwtTestUtil() {
        return new JwtTestUtil();
    }
}