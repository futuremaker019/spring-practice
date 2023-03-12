package com.sp.fc.web.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sp.fc.user.domain.SpUser;

import java.time.Instant;

public class JWTUtil {

    private static final Algorithm ALGORITHM = Algorithm.HMAC256("goohyun");
    // 만료시간
    private static final long AUTH_TIME = 20 * 60;
    // refresh 토큰 생성시간
    private static final long REFRESH_TIME = 60 * 60 * 24 * 7;

    /**
     * auth token을 생성
     * token내에는 user 정보를 넣어줘야 함
     */
    public static String makeAuthToken(SpUser user) {
        // 로그인한 사용자의 username을 담은 token을 생성한다.
        return JWT.create().withSubject(user.getUsername())
                // 만료시간을 담는다.
                .withClaim("exp", Instant.now().getEpochSecond() + AUTH_TIME)
                .sign(ALGORITHM);
    }

    /**
     * Refresh 토큰 생성
     */
    public String makeRefreshToken(SpUser user) {
        return JWT.create().withSubject(user.getUsername())
                // 만료시간을 담는다.
                .withClaim("exp", Instant.now().getEpochSecond() + REFRESH_TIME)
                .sign(ALGORITHM);
    }

    public static VerifyResult verify(String token) {
        try {
            DecodedJWT verify = JWT.require(ALGORITHM).build().verify(token);
            return VerifyResult.builder()
                    .success(true)
                    .username(verify.getSubject()).build();

        } catch (Exception ex) {
            DecodedJWT decode = JWT.decode(token);
            return VerifyResult.builder()
                    .success(false)
                    .username(decode.getSubject()).build();
        }
    }
}
