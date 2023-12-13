package com.alzzaipo.common.jwt;

import com.alzzaipo.common.LoginType;
import com.alzzaipo.common.Uid;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

	private static JwtProperties jwtProperties;

	@Autowired
	private JwtUtil(JwtProperties jwtProperties) {
		JwtUtil.jwtProperties = jwtProperties;
	}

    public static String createToken(Uid memberUID, LoginType loginType) {
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

    public static Uid getMemberUID(String token) {
        String serializedMemberUID = getClaims(token).get("memberUID", String.class);
        return Uid.fromJson(serializedMemberUID);
    }

    public static LoginType getLoginType(String token) {
        return LoginType.valueOf(getClaims(token).get("loginType", String.class));
    }

    private static Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token).getBody();
    }

    private static SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
