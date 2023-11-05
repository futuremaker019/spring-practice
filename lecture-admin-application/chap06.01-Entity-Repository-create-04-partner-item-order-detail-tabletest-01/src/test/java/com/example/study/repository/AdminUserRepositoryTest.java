package com.example.study.repository;

import com.example.study.StudyApplicationTests;
import com.example.study.entity.AdminUser;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class AdminUserRepositoryTest extends StudyApplicationTests {

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Test
    public void create() {

        AdminUser adminUser = new AdminUser();

        adminUser.setAccount("AdminUser01");
        adminUser.setPassword("AdminUser01");
        adminUser.setStatus("Registered");
        adminUser.setRole("PARTNER");
        adminUser.setCreatedAt(LocalDateTime.now());
        adminUser.setCreatedBy("AdminServer");

        AdminUser newAdminUser = adminUserRepository.save(adminUser);

        Assert.assertNotNull(newAdminUser);
    }
}