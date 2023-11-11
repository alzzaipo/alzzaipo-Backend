package com.alzzaipo.common.jwt;

import com.alzzaipo.common.LoginType;
import com.alzzaipo.common.Uid;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class NewJwtUtil {

    @Value("${jwt.secretKey}")
    private String secretKey;

    private final Long expireTimeMillis = 1000 * 60 * 30L;

    public String createToken(Uid memberUID, LoginType loginType) {
        Claims claims = Jwts.claims();
        claims.put("memberUID", memberUID.toJson());
        claims.put("loginType", loginType.name());

        SecretKey secretKey = getSecretKey();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMillis))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Uid getMemberUID(String token) {
        String serializedMemberUID = getClaims(token).get("memberUID", String.class);
        return Uid.fromJson(serializedMemberUID);
    }

    public LoginType getLoginType(String token) {
        return LoginType.valueOf(getClaims(token).get("loginType", String.class));
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token).getBody();
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
