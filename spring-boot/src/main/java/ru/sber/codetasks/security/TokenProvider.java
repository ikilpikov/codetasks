package ru.sber.codetasks.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

@Component
public class TokenProvider {
    private final Key secretKey;

    private final String issuer;

    public TokenProvider(@Value("${custom-properties.jwt.secret}") String secretKeyString,
                         @Value("${custom-properties.jwt.issuer}") String issuer) {
        this.secretKey = new SecretKeySpec(secretKeyString.getBytes(), "HmacSHA512");
        this.issuer = issuer;
    }

    public String createToken(String username, GrantedAuthority a) {
        var tokenExpirationDate = Date.from(ZonedDateTime.now().plusMinutes(120).toInstant());

        return Jwts.builder()
                .setClaims(Map.of("role", a.getAuthority(), "sub", username, "iss", issuer))
                .signWith(this.secretKey, SignatureAlgorithm.HS512)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(tokenExpirationDate)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String getClaim(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

}
