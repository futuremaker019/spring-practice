package kr.co.fastcampus.eatgo.util;

import org.junit.Test;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.*;

public class JwtUtilTests {

    @Test
    public void createToken() {
        String secret = "12345678901234567890123456789012";

        JwtUtil jwtUitl = new JwtUtil(secret);

        String token = jwtUitl.createToken(1004L, "John");

        assertThat(token, containsString("."));
    }
}