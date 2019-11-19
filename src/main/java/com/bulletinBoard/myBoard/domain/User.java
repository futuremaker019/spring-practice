package com.bulletinBoard.myBoard.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String userId;

    private String password;
    private String name;
    private String email;

    public void userUpdate(User user) {
        this.password = user.password;
        this.name = user.name;
        this.email = user.email;
    }

}
