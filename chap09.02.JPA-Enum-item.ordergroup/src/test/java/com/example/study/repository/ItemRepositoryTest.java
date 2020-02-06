package com.example.study.repository;

import com.example.study.StudyApplicationTests;
import com.example.study.entity.Item;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.*;

public class ItemRepositoryTest extends StudyApplicationTests {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void create() {
        Item item = new Item();
        item.setStatus("UNREGISTERED");
        item.setName("삼성 노트북");
        item.setTitle("삼성 노트북 A100");
        item.setPrice(900000);
        item.setBrandName("삼성");
        item.setRegisteredAt(LocalDateTime.now());
        item.setCreatedAt(LocalDateTime.now());
        item.setCreatedBy("Partner01");
//        item.setPartnerId(1L);   -> partner

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