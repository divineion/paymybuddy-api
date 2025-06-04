package com.paymybuddy.api.config;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

@Service
public class JWTService {

    private final JwtDecoder jwtDecoder;
	private JwtEncoder jwtEncoder;
	
	public JWTService(JwtEncoder jwtEncoder,  JwtDecoder jwtDecoder) {
		this.jwtEncoder = jwtEncoder;  
		this.jwtDecoder = jwtDecoder;
	}
	
	public String generateToken(CustomUserDetails user) {
		Instant now = Instant.now();

		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer("paymybuddy-api")
				.issuedAt(now)
				.expiresAt(now.plus(1, ChronoUnit.DAYS))
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

    public String extractUsername(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            return jwt.getSubject();
        } catch (JwtException e) {
            return null;
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            String username = jwt.getSubject();
            Instant expiration = jwt.getExpiresAt();

            boolean notExpired = expiration == null || Instant.now().isBefore(expiration);
            boolean usernameMatches = username != null && username.equals(userDetails.getUsername());

            return notExpired && usernameMatches;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}