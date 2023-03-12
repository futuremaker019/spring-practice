package com.sp.fc.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.xml.bind.DatatypeConverter;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

public class JWTSimpleTest {

    private void printToken(String token) {
        String[] tokens = token.split("\\.");
        System.out.println("header: " + new String(Base64.getDecoder().decode(tokens[0])));
        System.out.println("body : " + new String(Base64.getDecoder().decode(tokens[1])));
    }

    @DisplayName("1. jjwt 를 이용한 토큰 테스트")
    @Test
    void test_1() {
        // jjwt lib을 이용한 토큰 생성
        String oktaToken = Jwts.builder().addClaims(
                        Map.of("name", "goohyun", "price", 2000)
                ).signWith(SignatureAlgorithm.HS256, "goohyun")
                .compact();

        System.out.println("oktaToken = " + oktaToken);
        printToken(oktaToken);

        // 암호화된 토큰을 복호화
        Jws<Claims> tokenInfo = Jwts.parser().setSigningKey("goohyun").parseClaimsJws(oktaToken);
        System.out.println("body = " + tokenInfo);
    }


    @DisplayName("2. java-jwt 를 이용한 토큰 테스트")
    @Test
    void test_2() {
        /**
         * 알고리즘과 키값이 일치한다면 어떤 JWT 라이브러리를 사용하더라도 검증이 가능하다.
         */
        byte[] SEC_KEY = DatatypeConverter.parseBase64Binary("goohyun");

        // oath0 lib을 이용한 토큰 생성 방식
        String oauth0Token = JWT.create().withClaim("name", "goohyun").withClaim("price", 3000)
                .sign(Algorithm.HMAC256(SEC_KEY));
        System.out.println("oauth0Token = " + oauth0Token);
        printToken(oauth0Token);

        // 암호화된 토큰을 복화화
        DecodedJWT verified = JWT.require(Algorithm.HMAC256(SEC_KEY)).build().verify(oauth0Token);
        System.out.println("verified = " + verified.getClaims());

        Jws<Claims> tokenInfo = Jwts.parser().setSigningKey(SEC_KEY).parseClaimsJws(oauth0Token);
        System.out.println("tokenInfo = " + tokenInfo);
    }

    @DisplayName("3. 만료시간 테스트")
    @Test
    public void test_3() throws InterruptedException {
        Algorithm al = Algorithm.HMAC256("goohyun");

        String token = JWT.create().withSubject("a1234")
                .withNotBefore(new Date(System.currentTimeMillis() + 1000))
                .withExpiresAt(new Date(System.currentTimeMillis() + 3000))
                .sign(al);

        //Thread.sleep(2000);

        try {
            DecodedJWT verify = JWT.require(al).build().verify(token);
            System.out.println("verify.getClaims() = " + verify.getClaims());
        } catch (Exception e) {
            // not before 때문에 
            System.out.println("유효하지 않은 토큰입니다.");
            DecodedJWT decode = JWT.decode(token);
            System.out.println("decode.getClaims() = " + decode.getClaims());
        }


    }
}
