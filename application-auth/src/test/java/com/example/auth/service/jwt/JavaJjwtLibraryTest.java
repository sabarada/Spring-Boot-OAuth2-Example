package com.example.auth.service.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.jackson.io.JacksonSerializer;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JavaJjwtLibraryTest {


    @Test
    public void signed_jwt_create_with_registered_fields() {
        // given
        String issuer = "Karol";
        String subject = "Auth";
        String audience = "Karol";
        Date expiredAt = Date.from(Instant.now().plus(Duration.ofDays(1L)));
        Date NotBeforeAt = Date.from(Instant.now());
        Date issuedAt = Date.from(Instant.now());
        String jwtId = UUID.randomUUID().toString();

        final Key secret = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        final byte[] secretBytes = secret.getEncoded();
        final String base64SecretBytes = Base64.getEncoder().encodeToString(secretBytes);

        // when
        var key = Keys
                .hmacShaKeyFor(secretBytes);

        var jws = Jwts.builder()
                .setIssuer(issuer)
                .setSubject(subject)
                .setAudience(audience)
                .setExpiration(expiredAt)
                .setNotBefore(NotBeforeAt)
                .setIssuedAt(issuedAt)
                .setId(jwtId)
                .signWith(key)
                .compact();

        // then
        System.out.println(jws);
        System.out.println(base64SecretBytes);
    }

    @Test
    public void signed_jwt_verify_with_registered_fields() {
        // given
        String jws = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJLYXJvbCIsInN1YiI6IkF1dGgiLCJhdWQiOiJLYXJvbCIsImV4cCI6MTY3NjkwNzQyMywibmJmIjoxNjc2ODIxMDIzLCJpYXQiOjE2NzY4MjEwMjMsImp0aSI6IjNmNWZlMmUzLWJiM2MtNDBiNy04OTY0LWZkZmQzMjNmY2IwNyJ9.-AqaERWZj1_pc1bTrGNNZrqRmZ1zfg-4iD9lygZH5f0";

        // when
        final Key secret = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        final byte[] secretBytes = secret.getEncoded();

        var jwtSubject = Jwts.parserBuilder()
                .setSigningKey(Base64.getDecoder().decode("wbi3STeA4B2VG/LkXLiVf7zRjekTBeGZYyZvYrCcb4s="))
                .build();

        var parseClaimsJws = jwtSubject.parseClaimsJws(jws).getBody();

        assertEquals(parseClaimsJws.getIssuer(), "Karol");
        assertEquals(parseClaimsJws.getSubject(), "Auth");
        assertEquals(parseClaimsJws.getAudience(), "Karol");
        assertDoesNotThrow(() -> parseClaimsJws.getExpiration().toInstant().getEpochSecond());
        assertDoesNotThrow(() -> parseClaimsJws.getNotBefore().toInstant().getEpochSecond());
        assertDoesNotThrow(() -> parseClaimsJws.getIssuedAt().toInstant().getEpochSecond());
        assertDoesNotThrow(() -> UUID.fromString(parseClaimsJws.getId()));
    }

    @Test
    public void signed_jwt_create_with_private_field() throws JsonProcessingException {
        // given
        String email = "koangho93@naver.com";
        JwtUserDummy jwtUserDummy = new JwtUserDummy("karol", 31);

        final Key secret = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        final byte[] secretBytes = secret.getEncoded();
        final String base64SecretBytes = Base64.getEncoder().encodeToString(secretBytes);

        // when
        var key = Keys
                .hmacShaKeyFor(secretBytes);

        ObjectMapper objectMapper = new ObjectMapper();

        var jws = Jwts.builder()
                .serializeToJsonWith(new JacksonSerializer(objectMapper))
                .setClaims(Map.of("email", email, "user", jwtUserDummy))
                .signWith(key)
                .compact();

        System.out.println(jws);

        var jwtSubject = Jwts.parserBuilder()
                .deserializeJsonWith(new JacksonDeserializer(objectMapper))
                .setSigningKey(key)
                .build();

        var parseClaimsJws = jwtSubject
                .parseClaimsJws(jws)
                .getBody();

        // then
        assertEquals(parseClaimsJws.get("email", String.class), email);
        assertEquals(objectMapper.convertValue(parseClaimsJws.get("user"), JwtUserDummy.class).toString(), jwtUserDummy.toString());
    }

    /**
     * iss (Issuer) : JWT를 발급한 주체
     * sub (Subject) : JWT의 발급의 목적(주제)
     * aud (Audience) : JWT 발급받은 수신자
     * - 해당 값으로 식별이 가능해야합니다.
     * exp (Expiration Time) : JWT 만료시간
     * nbf (Not Before) : JWT 활성화 시간
     * iat (Issued At) : JWT 발급 시간
     * jti (JWT ID) : JWT 발급의 Unique ID
     */
    @Test
    public void signed_jwt_create_and_verify() {
        // given
        String issuer = "Karol";
        String subject = "Auth";
        String audience = "Karol";
        Date expiredAt = Date.from(Instant.now().plus(Duration.ofDays(1L)));
        Date NotBeforeAt = Date.from(Instant.now());
        Date issuedAt = Date.from(Instant.now());
        String jwtId = UUID.randomUUID().toString();

        // when
        var key = Keys
                .secretKeyFor(SignatureAlgorithm.HS256);

        var jws = Jwts.builder()
                .setIssuer(issuer)
                .setSubject(subject)
                .setAudience(audience)
                .setExpiration(expiredAt)
                .setNotBefore(NotBeforeAt)
                .setIssuedAt(issuedAt)
                .setId(jwtId)
                .signWith(key)
                .compact();

        System.out.println(jws);

        var jwtSubject = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();

        var parseClaimsJws = jwtSubject.parseClaimsJws(jws).getBody();

        // then
        assertEquals(parseClaimsJws.getIssuer(), issuer);
        assertEquals(parseClaimsJws.getSubject(), subject);
        assertEquals(parseClaimsJws.getAudience(), audience);
        assertEquals(parseClaimsJws.getExpiration().toInstant().getEpochSecond(),
                expiredAt.toInstant().getEpochSecond());
        assertEquals(parseClaimsJws.getNotBefore().toInstant().getEpochSecond(),
                NotBeforeAt.toInstant().getEpochSecond());
        assertEquals(parseClaimsJws.getIssuedAt().toInstant().getEpochSecond(),
                issuedAt.toInstant().getEpochSecond());
        assertEquals(parseClaimsJws.getId(), jwtId);
    }

}
