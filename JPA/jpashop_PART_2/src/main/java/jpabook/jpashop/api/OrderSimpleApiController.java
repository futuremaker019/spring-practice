package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * xToOne(ManyToOne, OneToOne)
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> orderV1() {
        List<Order> all = orderRepository.findAllByCriteria(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();        // Lazy 강제 초기화 -> 디비에서 값을 조회해온다.
            order.getDelivery().getAddress();   // Lazy 강제 초기화
        }

        return all;
    }

    /**
     * DTO에 담아서 보내면 API 스펙에 최적화하여 응답을 보낼수 있다.
     */
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> orderV2() {

        // ORDER 2개가 검색이된다.
        // N + 1 -> 1 주문 + 회원 N + 배송 N
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        // 첫번재 주문서 1개에 대한 멤버, 딜리버리의 쿼리가 나간다. 2개의 주문서기때문에 멤버, 딜리버리는 각각 2개씩 select 쿼리가 나간다.
        List<SimpleOrderDto> result = orders.stream()
//                .map(o -> new SimpleOrderDto(o))  // 아래 람다식 형태로 변경이 가능하다.
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList());
        return result;
    }

    /**
     * fetch join을 이용하여 쿼리가 N개 나가는것을 방지한다.
     */
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> orderV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        return orders.stream().map(o -> new SimpleOrderDto(o)).collect(Collectors.toList());
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();             // LAZY 초기화 (영속성 컨텍스트에 없기때문에 디비에서 가져온다.)
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();     // LAZY 초기화
        }
    }
}
