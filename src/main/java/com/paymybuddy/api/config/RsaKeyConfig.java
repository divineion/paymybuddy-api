package com.paymybuddy.api.config;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class RsaKeyConfig {
	 @Value("${jwt.public-key-location}")
	    private Resource publicKeyResource;

	    @Value("${jwt.private-key-location}")
	    private Resource privateKeyResource;

	    @Bean
	    RSAPublicKey publicKey() throws Exception {
	        try (InputStream inputStream = publicKeyResource.getInputStream()) {
	            String publicKeyPEM = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8)
	                .replace("-----BEGIN PUBLIC KEY-----", "")
	                .replace("-----END PUBLIC KEY-----", "")
	                .replaceAll("\\s+", "");

	            byte[] keyBytes = Base64.getDecoder().decode(publicKeyPEM);
	            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	            return (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));
	        }
	    }

	    @Bean
	    RSAPrivateKey privateKey() throws Exception {
	        try (InputStream inputStream = privateKeyResource.getInputStream()) {
	            String privateKeyPEM = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8)
	                .replace("-----BEGIN PRIVATE KEY-----", "")
	                .replace("-----END PRIVATE KEY-----", "")
	                .replaceAll("\\s+", "");

	            byte[] keyBytes = Base64.getDecoder().decode(privateKeyPEM);
	            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	            return (RSAPrivateKey) keyFactory.generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
	        }
	    }
}
