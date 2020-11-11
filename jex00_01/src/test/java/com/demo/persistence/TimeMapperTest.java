package com.demo.persistence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.demo.config.RootConfig;
import com.demo.mapper.TimeMapper;

import lombok.extern.log4j.Log4j;

@Log4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {com.demo.config.RootConfig.class})
public class TimeMapperTest {

	@Autowired
	private TimeMapper timeMapper;
	
	@Test
	public void testTimeCall() {
		log.info("timeMapper.getTime2() : " + timeMapper.getTime2());
	}
}
