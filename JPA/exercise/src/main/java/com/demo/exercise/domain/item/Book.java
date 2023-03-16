package com.demo.exercise.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Book")    // 구분값을 준다.
@Getter
@Setter
public class Book extends Item{
    private String author;
    private String isbn;
}