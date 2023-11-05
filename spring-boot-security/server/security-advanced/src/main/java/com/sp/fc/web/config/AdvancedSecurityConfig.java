package com.sp.fc.web.config;

import com.sp.fc.user.service.SpUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AdvancedSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    SpUserService spUserService;

    @Bean
    PasswordEncoder passwordEncoder() {
        // 암호화하지 않고 사용
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /**
         * 모바일과 통신한다는 전제로 작업한다.
         *  1. csrf는 서버에서 받아서 로컬에 보내놓고 받아오는 통신으로는 많은 비용이 많이드므로 disable
         *
         *  2. session을 사용하지 않는 방식으로 만든다. (authentication, authorization 의 문제를 플어야 한다.)
         *      2-1. 토큰을 이용한 Login filter를 구현하여 authentication 을 한다.
         *      2-2. 토큰을 이용한 check filter를 구현하여 authorization 을 한다.
         */
        JWTLoginFilter loginFilter = new JWTLoginFilter(authenticationManager(), spUserService);
        JWTCheckFilter checkFilter = new JWTCheckFilter(authenticationManager(), spUserService);

        http
                .csrf().disable()
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(checkFilter, BasicAuthenticationFilter.class)
                ;
    }
}
