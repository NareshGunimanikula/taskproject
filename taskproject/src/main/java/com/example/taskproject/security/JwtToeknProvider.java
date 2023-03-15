package com.example.taskproject.security;

import java.util.Date;

import org.springframework.security.config.annotation.rsocket.RSocketSecurity.JwtSpec;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Component
public class JwtToeknProvider {

	public String generateToekn(Authentication authentication) {
		String email = authentication.getName();
		Date currentDate = new Date();
		Date expireDate = new Date(currentDate.getTime()+3600000);
		
		String token = Jwts.builder()
				.setSubject(email)
				.setIssuedAt(currentDate)
				.setExpiration(expireDate)
				.signWith(SignatureAlgorithm.HS512, "JWTSecretKey")
				.compact();
		return token;
	}
	
	public String getEmailFromToekn(String token) {
		Claims claims = Jwts.parser().setSigningKey("JWTSecretKey")
		.parseClaimsJws(token).getBody();
		
		return claims.getSubject();
	}
}
