package com.f8.turnera.config;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.f8.turnera.security.domain.entities.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenUtil {

	@Value("${jwt.secret.key}")
    private static String secretKey;

	private TokenUtil() {
	}

	public static String generateToken(Authentication authentication) {
		String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));

		return Jwts.builder().setSubject(((User) authentication.getPrincipal()).getUsername())
				.claim(SecurityConstants.PERMISSIONS_KEY, authorities)
				.signWith(SignatureAlgorithm.HS256, secretKey).setIssuer("f8")
				.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME * 1000))
				.compact();
	}

	public static UsernamePasswordAuthenticationToken getAuthentication(final String token) {

		final JwtParser jwtParser = Jwts.parser().setSigningKey(secretKey);

		final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);

		final Claims claims = claimsJws.getBody();

		final Collection<SimpleGrantedAuthority> authorities = Arrays
				.stream(claims.get(SecurityConstants.PERMISSIONS_KEY).toString().split(","))
				.map(SimpleGrantedAuthority::new).collect(Collectors.toList());

		org.springframework.security.core.userdetails.User principal = new org.springframework.security.core.userdetails.User(
				claims.getSubject(), "", authorities);

		return new UsernamePasswordAuthenticationToken(principal, token, authorities);
	}

	public static String getUsernameWithoutToken() {
		org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return user.getUsername();
	}

	public static Object getClaimByToken(String token, String claimKey) {
		token = token.replace(SecurityConstants.TOKEN_PREFIX, "");

		final byte[] apiKeySecretBytes = secretKey.getBytes();
		final Key signingKey = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
		Claims claims = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token).getBody();

		return claims.get(claimKey);
	}

}