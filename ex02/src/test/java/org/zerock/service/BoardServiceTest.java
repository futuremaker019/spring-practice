package org.zerock.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zerock.domain.BoardVO;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class BoardServiceTest {

	@Setter(onMethod_ = @Autowired)
	private BoardService boardService;

	/*
	 * @Test public void testExist() {
	 * 
	 * log.info(boardService); assertNotNull(boardService); }
	 * 
	 * @Test public void testRegister() { BoardVO boardVO = new BoardVO();
	 * 
	 * boardVO.setTitle("새로 작성하는 글"); boardVO.setContent("새로 작성하는 내용");
	 * boardVO.setWriter("newbie");
	 * 
	 * boardService.register(boardVO);
	 * 
	 * log.info("생성된 게시물의 번호 : " + boardVO.getBno()); }
	 */

	/*
	 * @Test public void testGetList() { boardService.getList().forEach(board ->
	 * log.info(board)); }
	 * 
	 * @Test public void testGet() { log.info(boardService.get(1L)); }
	 */
	
	@Test
	public void testModify() {
		BoardVO boardVO = boardService.get(1L);
		
		if (boardVO == null) {
			return;
		}
		
		boardVO.setTitle("제목 수정합니다.");
		log.info("Modify Result : " + boardService.modify(boardVO));
	}
	
	@Test
	public void testDelete() {
		log.info("Remove Result : " + boardService.remove(2L));
	}
}
