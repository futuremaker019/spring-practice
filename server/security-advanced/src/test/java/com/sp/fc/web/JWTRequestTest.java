package com.sp.fc.web;

import com.sp.fc.user.domain.SpUser;
import com.sp.fc.user.repository.SpUserRepository;
import com.sp.fc.user.service.SpUserService;
import com.sp.fc.web.config.UserLoginForm;
import com.sp.fc.web.test.WebIntegrationTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.*;

public class JWTRequestTest extends WebIntegrationTest {

    @Autowired
    private SpUserRepository userRepository;

    @Autowired
    private SpUserService userService;

    @BeforeEach
    void before() {
        userRepository.deleteAll();

        SpUser user = userService.save(SpUser.builder()
                .email("user1")
                .password("1111")
                .enabled(true)
                .build());
        userService.addAuthority(user.getUserId(), "ROLE_USER");
    }

    private String getToken() {
        RestTemplate client = new RestTemplate();

        HttpEntity<UserLoginForm> body = new HttpEntity<>(
                UserLoginForm.builder().username("user1").password("1111").build()
        );

        ResponseEntity<SpUser> resp1 = client.exchange(uri("/login"), HttpMethod.POST, body, SpUser.class);

        return resp1.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0).substring("Bearer ".length());

    }

    @DisplayName("1. hello 메시지를 받아온다.")
    @Test
    public void test_1() {

        String token = getToken();

        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+token);
        HttpEntity body = new HttpEntity<>(null, headers);
        ResponseEntity<String> resp2 = client.exchange(uri("/greeting"), HttpMethod.GET, body, String.class);


        assertThat("hello").isEqualTo(resp2.getBody());
    }

    @DisplayName("2. 토큰 만료 테스트")
    @Test
    public void test_2() throws Exception {
        String token = getToken();

        Thread.sleep(3000);

        RestTemplate client = new RestTemplate();
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+token);
            HttpEntity body = new HttpEntity<>(null, headers);
            ResponseEntity<String> resp2 = client.exchange(uri("/greeting"), HttpMethod.GET, body, String.class);
        });
    }
}
