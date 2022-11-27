package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery")
    private Order order;

    @Embedded
    private Address address;

    // ORDINAL을 사용하면 숫자형식으로 들어간다.
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; //READY, COMP
}
