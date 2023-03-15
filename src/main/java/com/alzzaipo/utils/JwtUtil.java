package com.alzzaipo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {

    public static String createToken(String uid, String key, long expireTimeMillis) {
        Claims claims = Jwts.claims();
        claims.put("uid", uid);

        byte[] keyBytes = Decoders.BASE64.decode(key);
        SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMillis))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public static boolean isExpired(String token, String secretKey) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody()
                .getExpiration().before(new Date());

    }

    public static String getUid(String token, String secretKey) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody()
                .get("uid", String.class);
    }
}
