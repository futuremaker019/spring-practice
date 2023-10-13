package com.example.jwttutorial.repository;

import com.example.jwttutorial.entity.UserAccount;
import com.example.jwttutorial.util.SecurityUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @DisplayName("")
    @Test
    public void test() {
        String username = "admin";
        Optional<UserAccount> userAccount = repository.findOneWithAuthoritiesByUsername(username);


    }

}