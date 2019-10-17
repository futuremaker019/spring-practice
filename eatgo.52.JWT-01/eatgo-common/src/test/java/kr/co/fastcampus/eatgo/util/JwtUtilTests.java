package kr.co.fastcampus.eatgo.util;

import org.junit.Test;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.*;

public class JwtUtilTests {

    @Test
    public void createToken() {
        JwtUtil jwtUitl = new JwtUtil();

        String token = jwtUitl.createToken(1004L, "John");

        assertThat(token, containsString("."));
    }

}