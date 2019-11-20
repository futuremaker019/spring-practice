package com.bulletinBoard.myBoard.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
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
