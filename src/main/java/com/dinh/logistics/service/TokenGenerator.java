package com.dinh.logistics.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenGenerator {
	@Value("${app.jwtSecret}")
    private String jwtSecret;
	
	@Value("${app.accessToken.expiresTime}")
    private int accessTokenExpireTime;

    //generate token
    public String generateToken(String username, String password) {
        // Thời gian hết hạn của token
        long expirationMillis = System.currentTimeMillis() + accessTokenExpireTime;
        Date expirationDate = new Date(expirationMillis);

        String token = Jwts.builder()
                .setSubject(username)
                .claim("password", password)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();

        return token;
    }
    
    // decode token
    public Claims decodeToken(String token) {
        Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token);

        Claims claims = claimsJws.getBody();

//        String username = claims.getSubject();
//        String password = (String) claims.get("password");

        return claims;
    }
}
