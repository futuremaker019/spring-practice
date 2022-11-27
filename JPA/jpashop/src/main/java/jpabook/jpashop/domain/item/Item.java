package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")    // 구분 컬럼을 지정해준다.
@Getter @Setter
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "itme_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

//    @OneToMany()
//    private List<OrderItem> orderItems = new ArrayList<>();

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();
}
