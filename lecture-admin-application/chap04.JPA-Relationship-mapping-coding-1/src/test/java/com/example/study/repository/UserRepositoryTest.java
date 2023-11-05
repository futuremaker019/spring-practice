package com.example.study.repository;

import com.example.study.StudyApplicationTests;
import com.example.study.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

public class UserRepositoryTest extends StudyApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void create() {
        User user = new User();
        user.setAccount("TestUser01");
        user.setEmail("TestUser01@gmail.com");
        user.setPhoneNumber("010-1111-3333");
        user.setCreatedAt(LocalDateTime.now());
        user.setCreatedBy("TestUser3");

        User newUser = userRepository.save(user);

        System.out.println("newUser = " + newUser);
    }

    @Test
    public void read() {
        Optional<User> user = userRepository.findById(3L);

        user.ifPresent(selectUser -> {
            System.out.println("user1 = " + selectUser);
            System.out.println("selectUser.getEmail() = " + selectUser.getEmail());
        });
    }

    @Test
    public void update() {
        Optional<User> user = userRepository.findById(3L);

        user.ifPresent(selectUser -> {
            selectUser.setAccount("pppp");
            selectUser.setUpdatedAt(LocalDateTime.now());
            selectUser.setUpdatedBy("update method()");

            userRepository.save(selectUser);
        });
    }

    @Test
    @Transactional
    public void delete() {
        Optional<User> user = userRepository.findById(4L);

        Assert.assertTrue(user.isPresent());

        user.ifPresent(selectUser -> {
            userRepository.delete(selectUser);
        });

        Optional<User> deleteUser = userRepository.findById(4L);

        Assert.assertFalse(deleteUser.isPresent());

    }
}
