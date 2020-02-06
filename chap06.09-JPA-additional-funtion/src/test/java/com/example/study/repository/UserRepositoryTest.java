package com.example.study.repository;

import com.example.study.StudyApplicationTests;
import com.example.study.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class UserRepositoryTest extends StudyApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void create() {
        String account = "Test03";
        String password = "Test03";
        String status = "REGISTERED";
        String email = "Test01@gmail.com";
        String phoneNumber = "010-1111-3333";
        LocalDateTime registeredAt = LocalDateTime.now();
        LocalDateTime createdAt = LocalDateTime.now();
        String createdBy = "AdminServer";

        User user = new User();

        user.setAccount(account);
        user.setPassword(password);
        user.setStatus(status);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setRegisteredAt(registeredAt);

        // JPA Auditing을 이용해 update를 이용한 date변경 적용
//        user.setCreatedAt(createdAt);
//        user.setCreatedBy(createdBy);

        // builder 패턴을 사용하여 객체를 생성함
//        User u = User.builder()
//                .account(account)
//                .password(password)
//                .status(status)
//                .email(email)
//                .build();

        User newUser = userRepository.save(user);

        Assert.assertNotNull(newUser);
//        assertThat(user.getAccount(), is("Test01"));

    }

    @Test
    @Transactional
    public void read() {
        User user = userRepository.findFirstByPhoneNumberOrderByIdDesc("010-1111-2222").orElse(null);

        // @Accessors(chain = ture)를 이용한 setter 체인
//        user
//            .setEmail("")
//            .setPhoneNumber("")
//            .setStatus("")

        user.getOrderGroupList().stream().forEach(orderGroup -> {

            System.out.println("------------------주문묶음----------------------");
            System.out.println("수령인 : " + orderGroup.getRevName());
            System.out.println("수령지 : " + orderGroup.getRevAddress());
            System.out.println("총금액 : " + orderGroup.getTotalPrice());
            System.out.println("총수량 : " + orderGroup.getTotalQuantity());

            System.out.println("------------------주문상세----------------------");

            orderGroup.getOrderDetailList().stream().forEach(orderDetail -> {
                System.out.println("파트너사 이름 : " + orderDetail.getItem().getPartner().getName());
                System.out.println("파트너사 카테고리 : " + orderDetail.getItem().getPartner().getCategory().getType());
                System.out.println("주문 상품 : " + orderDetail.getItem().getName());
                System.out.println("고객센터 번호 : " + orderDetail.getItem().getPartner().getCallCenter());
                System.out.println("주문의 상태 : " + orderDetail.getStatus());
                System.out.println("도착예정일자 : " + orderDetail.getArrivalDate());

            });
        });

//        Assert.assertNotNull(user);
        assertThat(user.getAccount(), is("Test01"));
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
