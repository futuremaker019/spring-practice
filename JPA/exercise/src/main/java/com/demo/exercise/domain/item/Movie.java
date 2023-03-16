package com.demo.exercise.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

@Entity
@DiscriminatorValue("Movie")
@Getter @Setter
public class Movie extends Item{

    private String artist;
    private String etc;
}
