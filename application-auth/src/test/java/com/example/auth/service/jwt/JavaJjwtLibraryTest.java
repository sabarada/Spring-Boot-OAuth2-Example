package com.example.auth.service.jwt;

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

    /**
     * signed_jwt(jws)를 만들고 이를 parsing, payload를 검증하는 테스트
     *
     * iss (Issuer) : JWT를 발급한 주체
     * sub (Subject) : JWT의 발급의 목적(주제)
     * aud (Audience) : JWT 발급받은 수신자
     * - 해당 값으로 식별이 가능해야합니다.
     * exp (Expiration Time) : JWT 만료시간
     * nbf (Not Before) : JWT 활성화 시간
     * iat (Issued At) : JWT 발급 시간
     * jti (JWT ID) : JWT 발급의 Unique ID
     *
     * create 로직
     * 1. jwt에 sign할 Key를 생성합니다. 여기서는 (JS256 알고리즘을 사용)
     * 2. 만들어진 key의 base64SecretBytes를 생성합니다. 이것은 생성된 키를 테스트 이외에서도 사용하기 위해서 입니다.
     * 3. signed jwt를 생성합니다.
     *
     * verify 로직
     * 1. jws를 base64SecretBytes를 이용하여 parsing 합니다.
     * 2.parsing된 payload 필드를 검증합니다.
     */
    @Test
    public void signed_jwt_create_and_verify_with_registered_fields() {
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

        // when (create)
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

        // peak
        System.out.println(jws);
        System.out.println(base64SecretBytes);

        // when (verify)
        var jwtSubject = Jwts.parserBuilder()
                .setSigningKey(Base64.getDecoder().decode(base64SecretBytes))
                .build();

        var parseClaimsJws = jwtSubject.parseClaimsJws(jws).getBody();

        // then
        assertEquals(parseClaimsJws.getIssuer(), "Karol");
        assertEquals(parseClaimsJws.getSubject(), "Auth");
        assertEquals(parseClaimsJws.getAudience(), "Karol");
        assertDoesNotThrow(() -> parseClaimsJws.getExpiration().toInstant().getEpochSecond());
        assertDoesNotThrow(() -> parseClaimsJws.getNotBefore().toInstant().getEpochSecond());
        assertDoesNotThrow(() -> parseClaimsJws.getIssuedAt().toInstant().getEpochSecond());
        assertDoesNotThrow(() -> UUID.fromString(parseClaimsJws.getId()));
    }

    /**
     * 커스텀(private) 필드가 많은 signed_jwt를 생성 및 검증합니다.
     *
     * 생성되는 payload는 아래와 같습니다.
     * {
     *   "email": "koangho93@naver.com",
     *   "user": {
     *     "nickname": "karol",
     *     "age": 31
     *   }
     * }
     *
     * JacksonSerializer / JacksonDeserializer를 사용합니다.
     * 필드 일부를 propertiy를 파싱하기 위해서는 json value인 key로 가져온 후 objectMapper로 파싱합니다.
     */
    @Test
    public void signed_jwt_create_with_private_field() {
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
        assertEquals(
                objectMapper.convertValue(parseClaimsJws.get("user"), JwtUserDummy.class).toString(),
                jwtUserDummy.toString()
        );
    }

    /**
     * 아래는 signed_jwt_create_and_verify_with_registered_fields() 테스트에서 key를
     * base64Secret으로 추출하지 않고 사용하는 방법입니다. 해당 방법은 간단한 방법이지만 실무에서는
     * key를 보존해야하는데 그렇지 않기 때문에 사용하기 어려운 방법입니다.
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
