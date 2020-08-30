package org.zerock.mapper;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyVO;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class ReplyMapperTest {
	
	private Long[] bnoArr = { 1179659L, 1179660L, 1179661L, 1179662L, 1179663L};
	
	@Setter(onMethod_ = @Autowired)
	private ReplyMapper mapper;
	
	
	/* @Test public void testMapper() { log.info(mapper); } */
	  
	/*
	 * @Test public void testCreate() { // 1에서 5의 반복수를 넣기 위해 intstream을 활용해였다.
	 * IntStream.rangeClosed(1, 10).forEach(i -> { ReplyVO vo = new ReplyVO();
	 * 
	 * vo.setBno(bnoArr[i % 5]); System.out.println(bnoArr[i%5]);
	 * vo.setReply("댓글 테스트  " + i); vo.setReplyer("replyer" + i);
	 * 
	 * mapper.insert(vo); }); }
	 */
	 
	
	/*
	 * @Test public void testRead() { Long targetRno = 5L;
	 * 
	 * log.info(mapper.read(targetRno)); }
	 */
	
	/*
	 * @Test public void testDelete() { Long targetRno = 1L;
	 * 
	 * mapper.delete(targetRno); }
	 */
	
	/*
	 * @Test public void testUpdate() { Long targetRno = 10L;
	 * 
	 * ReplyVO reply = mapper.read(targetRno);
	 * 
	 * reply.setReply("Update Reply");
	 * 
	 * int count = mapper.update(reply);
	 * 
	 * log.info("Update Count: " + count); }
	 */
	
	@Test
	public void testList() {
		
		Criteria cri = new Criteria();
		
		List<ReplyVO> replies = mapper.getListWithPaging(cri, bnoArr[0]);
		
		log.info(replies);
	}
}
