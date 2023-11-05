package com.demo.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class Restaurant {

	private Chef chef;
	
	@Autowired
	public Restaurant (Chef chef) {
		this.chef = chef;
	}
}
