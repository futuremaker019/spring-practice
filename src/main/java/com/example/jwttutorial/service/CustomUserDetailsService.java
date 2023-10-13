package com.example.jwttutorial.service;

import com.example.jwttutorial.entity.UserAccount;
import com.example.jwttutorial.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 로그인시에 DB에서 유저정보와 권한정보를 가져오게 된다.
 */

@RequiredArgsConstructor
@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)  {
        return userRepository.findOneWithAuthoritiesByUsername(username)
                .map(userAccount -> createUser(username, userAccount))
                .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
    }

    private User createUser(String username, UserAccount userAccount) {
        if (!userAccount.isActivated()) {
            throw new RuntimeException(username + " 활성화되있지 않은 유저입니다.");
        }

        List<GrantedAuthority> grantedAuthorities = userAccount.getAuthorities()
                .stream().map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                .collect(Collectors.toList());

        return new User(userAccount.getUsername(), userAccount.getPassword(), grantedAuthorities);
    }
}
