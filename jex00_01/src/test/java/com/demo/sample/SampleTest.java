package com.demo.sample;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.demo.config.RootConfig;

import lombok.extern.log4j.Log4j;

@Log4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {RootConfig.class})
public class SampleTest {
	
	@Autowired
	private Restaurant restaurant;

	@Test
	public void sampleTest() {
		log.info("restaurant instance created~~~~~: " + restaurant);
		assertNotNull(restaurant);
		
		log.info("-------------------------");
		log.info("restaurant.getChef() : " + restaurant.getChef());
	}
}

