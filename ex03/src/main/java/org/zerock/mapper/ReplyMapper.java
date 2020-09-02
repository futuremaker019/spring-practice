package org.zerock.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyVO;

public interface ReplyMapper {

	// insert의 반환형은 int이다??
	public int insert(ReplyVO reply);
	
	public ReplyVO read(Long rno);
	
	public int delete(Long rno);
	
	public int update(ReplyVO reply);
	
	public List<ReplyVO> getListWithPaging(@Param("cri") Criteria cri, 
											@Param("bno") Long bno);
	
	// 갑자기 드는 궁금증
	// 이 함수의 파라미터는 어디서 어떻게 받아오는가
	public int  getCountByBno(Long bno);
}
