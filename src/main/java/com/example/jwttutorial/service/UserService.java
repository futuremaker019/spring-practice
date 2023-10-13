package com.example.jwttutorial.service;

import com.example.jwttutorial.dto.UserDto;
import com.example.jwttutorial.entity.Authority;
import com.example.jwttutorial.entity.UserAccount;
import com.example.jwttutorial.repository.UserRepository;
import com.example.jwttutorial.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserAccount signup(UserDto userDto) {
        UserAccount savedUser = userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null);
        if (savedUser != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        UserAccount userAccount = UserAccount.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        return userRepository.save(userAccount);
    }

    @Transactional(readOnly = true)
    public Optional<UserAccount> getUserWithAuthorities(String username) {
        return userRepository.findOneWithAuthoritiesByUsername(username);
    }

    @Transactional(readOnly = true)
    public Optional<UserAccount> getLoginUserWithAuthorities() {

        /**
         * 로그인한 사용자의 이름 (SecurityContextHolder 에서 가져옴
         *  로그인한 사용자 이름을 검색하여 useraccount 객체를 가져온다.
         */

        return SecurityUtil.getCurrentUsername().map(userRepository::findOneWithAuthoritiesByUsername).get();
    }
}
