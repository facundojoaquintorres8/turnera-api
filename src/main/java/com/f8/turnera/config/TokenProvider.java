package com.f8.turnera.config;

import io.jsonwebtoken.*;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import com.f8.turnera.security.entities.User;

public class TokenProvider {

	private TokenProvider() {
	}

	public static String generateToken(Authentication authentication) {
		String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));

		return Jwts.builder().setSubject(((User) authentication.getPrincipal()).getUsername())
				.claim(SecurityConstants.AUTHORITIES_KEY, authorities)
				.signWith(SignatureAlgorithm.HS256, SecurityConstants.SECRET).setIssuer("f8")
				.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME * 1000))
				.compact();
	}

	public static UsernamePasswordAuthenticationToken getAuthentication(final String token) {

		final JwtParser jwtParser = Jwts.parser().setSigningKey(SecurityConstants.SECRET);

		final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);

		final Claims claims = claimsJws.getBody();

		final Collection<SimpleGrantedAuthority> authorities = Arrays
				.stream(claims.get(SecurityConstants.AUTHORITIES_KEY).toString().split(","))
				.map(SimpleGrantedAuthority::new).collect(Collectors.toList());

		org.springframework.security.core.userdetails.User principal = new org.springframework.security.core.userdetails.User(
				claims.getSubject(), "", authorities);

		return new UsernamePasswordAuthenticationToken(principal, token, authorities);
	}

	public static String getUsername(final String token) {
		final JwtParser jwtParser = Jwts.parser().setSigningKey(SecurityConstants.SECRET);

		final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);

		return claimsJws.getBody().getSubject();
	}

	public static String getUsernameWithoutToken() {
		org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return user.getUsername();
	}

}