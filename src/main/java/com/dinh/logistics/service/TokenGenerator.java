package com.dinh.logistics.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenGenerator {
    private static final String SECRET_KEY = ""; // Thay thế bằng khóa bí mật của bạn

    //generate token
    public String generateToken(String username, String password) {
        // Thời gian hết hạn của token (30 phút từ thời điểm hiện tại)
        long expirationMillis = System.currentTimeMillis() + (1 * 60 * 60 * 1000); // 1h     
        Date expirationDate = new Date(expirationMillis);

        String token = Jwts.builder()
                .setSubject(username)
                .claim("password", password)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        return token;
    }
    
    // decode token
    public Claims decodeToken(String token) {
        Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token);

        Claims claims = claimsJws.getBody();

//        String username = claims.getSubject();
//        String password = (String) claims.get("password");

        return claims;
    }
}
