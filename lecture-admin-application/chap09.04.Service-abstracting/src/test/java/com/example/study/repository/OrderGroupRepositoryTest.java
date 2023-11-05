package com.example.study.repository;

import com.example.study.StudyApplicationTests;
import com.example.study.entity.OrderGroup;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class OrderGroupRepositoryTest extends StudyApplicationTests {

    @Autowired
    private OrderGroupRepository orderGroupRepository;

    @Test
    public void create() {

        OrderGroup orderGroup = new OrderGroup();

        orderGroup.setStatus("Complete");
//        orderGroup.setOrderType("All");
        orderGroup.setRevAddress("서울시 강남구");
        orderGroup.setRevName("홍길동");
//        orderGroup.setPaymentType("CARD");
        orderGroup.setTotalPrice(BigDecimal.valueOf(900000));
        orderGroup.setTotalQuantity(1);
        orderGroup.setOrderAt(LocalDateTime.now().minusDays(2));
        orderGroup.setArrivalDate(LocalDateTime.now());
        orderGroup.setCreatedAt(LocalDateTime.now());
        orderGroup.setCreatedBy("AdminServer");
//        orderGroup.setUserId(1L);   //  -> User

        OrderGroup newOrderGroup = orderGroupRepository.save(orderGroup);

        Assert.assertNotNull(newOrderGroup);
    }
}