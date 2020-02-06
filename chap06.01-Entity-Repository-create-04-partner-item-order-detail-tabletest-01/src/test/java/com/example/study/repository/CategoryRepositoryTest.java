package com.example.study.repository;

import com.example.study.StudyApplicationTests;
import com.example.study.entity.Category;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.*;

public class CategoryRepositoryTest extends StudyApplicationTests {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void create() {
        String type = "COMPUTER0";
        String title = "컴퓨터";
        LocalDateTime createdAt = LocalDateTime.now();
        String createdBy = "AdminServer";

        Category category = new Category();

        category.setType(type);
        category.setTitle(title);
        category.setCreatedAt(createdAt);
        category.setCreatedBy(createdBy);

        Category newCategory = categoryRepository.save(category);

        Assert.assertNotNull(newCategory);
        Assert.assertEquals(newCategory.getType(), type);
        Assert.assertEquals(newCategory.getTitle(), title);
    }

    @Test
    public void read() {
        Optional<Category> category = categoryRepository.findByType("COMPUTER0");

//        Optional<Category> category = categoryRepository.findById(1L);

        // select * from category where type = "COMPUTER"

        category.ifPresent(c -> {
            System.out.println(c.getId());
            System.out.println(c.getType());
            System.out.println(c.getTitle());
        });


    }
}