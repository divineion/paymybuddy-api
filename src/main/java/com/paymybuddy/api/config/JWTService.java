package com.paymybuddy.api.config;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JWTService {
	private JwtEncoder jwtEncoder;
	
	public JWTService(JwtEncoder jwtEncoder) {
		this.jwtEncoder = jwtEncoder;  
	}
	
	public String generateToken(CustomUserDetails user) {
		Instant now = Instant.now();
				// définir le payload
		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer("paymybuddy-api")
				.issuedAt(now)
				//TODO expiration 1 DAY
				.expiresAt(now.plus(10, ChronoUnit.DAYS))
				.subject(user.getUsername()) // email
				.claim("id", user.getId())
				.claim("authorities", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
				.build();
		
		JwtEncoderParameters parameters = JwtEncoderParameters.from(
				JwsHeader.with(SignatureAlgorithm.RS256)
				.keyId(ConfigConstants.RSA_KEY_ID)
				.build(),
			claims);
		
		return this.jwtEncoder.encode(parameters).getTokenValue();
	}
	
}
