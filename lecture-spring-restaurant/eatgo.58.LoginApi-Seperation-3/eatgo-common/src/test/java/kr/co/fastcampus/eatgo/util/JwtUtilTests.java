package kr.co.fastcampus.eatgo.util;

import io.jsonwebtoken.Claims;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.*;

public class JwtUtilTests {

    private static final String secret = "12345678901234567890123456789012";

    private JwtUtil jwtUitl;

    @Before
    public void setUp() {
        jwtUitl = new JwtUtil(secret);
    }

    @Test
    public void createToken() {
        String token = jwtUitl.createToken(1004L, "John", null);

        assertThat(token, containsString("."));
    }

    @Test
    public void getClaims() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEwMDQsIm5hbWUiOiJKb2huIn0.8hm6ZOJykSINHxL-rf0yV882fApL3hyQ9-WGlJUyo2A";

        Claims claims = jwtUitl.getClaims(token);

        assertThat(claims.get("userId", Long.class), is(1004L));
        assertThat(claims.get("name"), is("John"));
    }
}