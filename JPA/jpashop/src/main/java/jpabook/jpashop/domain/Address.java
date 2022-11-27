package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable     // 내장타입임을 선언해준다.
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    // 기본 생성자가 필요하다.
    protected Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
