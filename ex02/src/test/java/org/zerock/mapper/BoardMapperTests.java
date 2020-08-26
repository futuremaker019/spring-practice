package org.zerock.mapper;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class BoardMapperTests {
	
	@Setter(onMethod_ = {@Autowired})
	private BoardMapper boardMapper;
	
	/*
	 * @Test public void testGetList() { boardMapper.getList().forEach(board ->
	 * log.info(board)); }
	 */
	
	/*
	 * @Test public void testInsert() { BoardVO board = new BoardVO();
	 * board.setTitle("새로 작성하는 글"); board.setContent("새로 작성하는 내용");
	 * board.setWriter("newbie");
	 * 
	 * boardMapper.insert(board);
	 * 
	 * log.info(board); }
	 */
	
	/*
	 * @Test public void testInsertSelectKey() { BoardVO board = new BoardVO();
	 * board.setTitle("새로 작성하는 글 select key");
	 * board.setContent("새로 작성하는 내용 select key"); board.setWriter("newbie");
	 * 
	 * boardMapper.insertSelectKey(board);
	 * 
	 * log.info(board); }
	 */
	
	/*
	 * @Test public void readTest() { BoardVO boardVO = boardMapper.read(5L);
	 * 
	 * log.info(boardVO); }
	 */
	
	/*
	 * @Test public void testDelete() { int count = boardMapper.delete(3L);
	 * 
	 * log.info("Delete Count : " + count); }
	 */
	
	/*
	 * @Test public void testUpdate() { BoardVO boardVO = new BoardVO();
	 * 
	 * // setter를 이용하여 입력된 값이 변수로 들어간다. mapper 객체에 전달되어 mapper.xml을 실행
	 * boardVO.setBno(5L); 
	 * boardVO.setTitle("수정된 제목"); 
	 * boardVO.setContent("수정된 내용");
	 * boardVO.setWriter("modified user");
	 * 
	 * int count = boardMapper.update(boardVO);
	 * 
	 * log.info("Update Count : " + count); }
	 */
	
	/*
	 * @Test public void testPaging() { Criteria cri = new Criteria(); //10개씩 3페이지
	 * cri.setPageNum(3); cri.setAmount(10);
	 * 
	 * List<BoardVO> list = boardMapper.getListWithPaging(cri);
	 * 
	 * list.forEach(board -> log.info(board.getBno())); }
	 */
	
	@Test
	public void testSearch() {
		
		Criteria cri = new Criteria();
		cri.setKeyword("새로");
		cri.setType("TC");
		
		List<BoardVO> list = boardMapper.getListWithPaging(cri);
		
		list.forEach(board -> log.info(board));
	}
}
