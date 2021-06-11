package com.weather.weathermonitor.security.jwt;

import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.auth0.jwk.JwkException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtils {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	private static final String AUTHORITIES_KEY = "scopes";
	@Autowired
	RSAPublicKey publicKey;
	private final String HEADER = "Authorization";
	private final String PREFIX = "Bearer ";
	@Value("${auth-service.url1}")
	private String url;

	public boolean validateJwtToken(String jwtToken, HttpServletRequest request) throws JwkException {
		try {
			jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
			DecodedJWT jwt = JWT.decode(jwtToken);
			Algorithm algorithm = Algorithm.RSA512((RSAPublicKey) publicKey, null);
			algorithm.verify(jwt);
			return true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}

	public Authentication getAuthentication(String token, HttpServletRequest request) throws ExpiredJwtException,
			UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, JwkException {

		Claims claims = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();
		Collection<? extends GrantedAuthority> authorities = Arrays
				.asList(claims.get(AUTHORITIES_KEY).toString().split(",")).stream()
				.map(authority -> new SimpleGrantedAuthority(authority)).collect(Collectors.toList());

		User principal = new User(claims.getSubject(), "", authorities);

		return new UsernamePasswordAuthenticationToken(principal, "", authorities);
	}

}