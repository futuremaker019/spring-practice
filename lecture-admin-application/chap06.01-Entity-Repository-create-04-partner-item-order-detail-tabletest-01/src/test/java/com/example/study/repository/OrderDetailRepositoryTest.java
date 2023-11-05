package com.example.study.repository;

import com.example.study.StudyApplicationTests;
import com.example.study.entity.OrderDetail;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderDetailRepositoryTest extends StudyApplicationTests {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Test
    public void create() {

        OrderDetail orderDetail = new OrderDetail();

        orderDetail.setStatus("Waiting");
        orderDetail.setArrivalDate(LocalDateTime.now().plusDays(2));
        orderDetail.setQuantity(1);
        orderDetail.setTotalPrice(BigDecimal.valueOf(900000));
        orderDetail.setOrderGroupId(1L);   // 어떠한 장바구니에
        orderDetail.setItemId(1L);          // 어떠한 상품
        orderDetail.setCreatedAt(LocalDateTime.now());
        orderDetail.setCreatedBy("Partner01");

        OrderDetail orderDetail1 = orderDetailRepository.save(orderDetail);

        Assert.assertNotNull(orderDetail);

//        orderDetail.setOrderAt(LocalDateTime.now());


        // setUserId에 에러가 난다.
//        orderDetail.setUserId(5L);

        // 어떤 물건?
//        orderDetail.setItemId(1L);


    }
}

// 먼저 user에서 테스트 진행하여 user를 만들어주고
// item 도 만들어준다.
// 만들어진 user와 item의 정보를 mySql에서 확인하여 아이디를 가져와
// order_detail의 테이블의 칼럼을 create를 통해 만들어준다.

// 어떤 사람?
// 처음에 userId를 만들때에 Long 형식으로 만들었기 때문에
// 지금은 연관관계 설정 때문에 데이터 형식이 User로 바뀌었기 때문에