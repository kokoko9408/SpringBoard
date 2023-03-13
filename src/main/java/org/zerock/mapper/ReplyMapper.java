package org.zerock.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.zerock.domain.Criterial;
import org.zerock.domain.ReplyVO;

public interface ReplyMapper {
	
	public int insert(ReplyVO vo);
	public ReplyVO read(Long rno); //읽어오기
	public int delete(Long rno);
	public int update(ReplyVO vo);
	public List<ReplyVO> getListWithPaging(
			@Param("cri")Criterial cri,
			@Param("bno") Long bno);
	public int getCountByBno(Long bno);
	
	
	

}
