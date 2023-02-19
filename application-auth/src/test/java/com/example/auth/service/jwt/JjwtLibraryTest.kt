package com.example.auth.service.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


internal class JjwtLibraryTest {

    @Test
    fun signed_jwt_create_and_verify() {
        // given
        val subject = "Karol"
        // - iss (Issuer) : JWT를 발급한 주체
        //- sub (Subject) : JWT의 발급의 목적(주제)
        //- aud (Audience) : JWT 발급받은 수신자
        //  - 해당 값으로 식별이 가능해야합니다.
        //- exp (Expiration Time) : JWT 만료시간
        //- nbf (Not Before) : JWT 활성화 시간
        //- iat (Issued At) : JWT 발급 시간
        //- jti (JWT ID) : JWT 발급의 Unique ID

        // when
        val key = Keys
            .secretKeyFor(SignatureAlgorithm.HS256)

        val jws = Jwts.builder()
            .setSubject(subject)
            .signWith(key)
            .compact()

        println(jws)

        val jwtSubject = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()

        val parseClaimsJws = jwtSubject.parseClaimsJws(jws)

        // then
        assertEquals(parseClaimsJws.body.subject, subject)
    }
}