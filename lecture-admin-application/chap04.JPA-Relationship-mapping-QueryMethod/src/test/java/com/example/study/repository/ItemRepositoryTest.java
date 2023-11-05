package com.example.study.repository;

import com.example.study.StudyApplicationTests;
import com.example.study.entity.Item;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.junit.Assert.*;

public class ItemRepositoryTest extends StudyApplicationTests {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void create() {

        Item item = new Item();
        item.setName("Laptop");
        item.setPrice(100000);
        item.setContent("Samsung");

        Item newItem = itemRepository.save(item);
        Assert.assertNotNull(newItem);

//        System.out.println("newItem = " + newItem);

    }

    @Test
    @Transactional
    public void read() {
//        Item item = itemRepository.findById(1L).get();

        Optional<Item> item = itemRepository.findById(1L);

        item.ifPresent(i -> {
            System.out.println("i = " + i);
        });

//        System.out.println("item = " + item);
    }
}