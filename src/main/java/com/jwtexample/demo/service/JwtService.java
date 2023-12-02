package com.jwtexample.demo.service;

import io.jsonwebtoken.Claims; 
import io.jsonwebtoken.Jwts; 
import io.jsonwebtoken.SignatureAlgorithm; 
import io.jsonwebtoken.io.Decoders; 
import io.jsonwebtoken.security.Keys; 
import org.springframework.security.core.userdetails.UserDetails; 
import org.springframework.stereotype.Component; 

import java.security.Key;
import java.util.Date; 
import java.util.HashMap;
import java.util.Map; 
import java.util.function.Function; 

@Component
public class JwtService { 

	public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437"; 
	public String generateToken(String userName, int userId) { 
		Map<String, Object> claims = new HashMap<>(); 
        claims.put("username", userName);
        claims.put("id", userId);
		return createToken(claims); 
	} 

	private String createToken(Map<String, Object> claims) { 
		return Jwts.builder() 
				.setClaims(claims)
				.setIssuedAt(new Date(System.currentTimeMillis())) 
				.setExpiration(new Date(System.currentTimeMillis() +  24 * 60 * 60 * 1000)) 
				.signWith(getSignKey(), SignatureAlgorithm.HS256).compact(); 
	} 

	private Key getSignKey() { 
		byte[] keyBytes= Decoders.BASE64.decode(SECRET); 
		return Keys.hmacShaKeyFor(keyBytes); 
	} 

	public String extractUsername(String token) { 
		return extractClaim(token, Claims::getSubject); 
	} 

	public Date extractExpiration(String token) { 
		return extractClaim(token, Claims::getExpiration); 
	} 

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) { 
		final Claims claims = extractAllClaims(token); 
		return claimsResolver.apply(claims); 
	} 

	private Claims extractAllClaims(String token) { 
		return Jwts 
				.parserBuilder() 
				.setSigningKey(getSignKey()) 
				.build() 
				.parseClaimsJws(token) 
				.getBody(); 
	}

	public Map<String, Object> extractUsernameAndUserId(String token) {
		final Claims claims = extractAllClaims(token);

		String username = claims.containsKey("username") ? claims.get("username", String.class) : null;
		Integer userId = claims.containsKey("id") ? claims.get("id", Integer.class) : null;

		Map<String, Object> userClaims = new HashMap<>();
		userClaims.put("username", username);
		userClaims.put("id", userId);

		return userClaims;
	}


	private Boolean isTokenExpired(String token) { 
		return extractExpiration(token).before(new Date()); 
	} 

	public Boolean validateToken(String token, UserDetails userDetails) { 
		final Map<String, Object> usernameandId = extractUsernameAndUserId(token); 

		Object username=usernameandId.get("username");
		
		// final String username = extractUsername(token); 
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token)); 
	} 


} 

