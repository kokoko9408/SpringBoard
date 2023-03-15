package org.zerock.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.zerock.domain.BoardAttachVO;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criterial;

public interface BoardMapper {
	
	//@Select("select * from tbl_board where bno > 0")
	public List<BoardVO> getList();
	public BoardVO read(Long bno);
	public void insert(BoardVO board);
	public void insertSelectKey(BoardVO board);
	public int delete(Long bno);
	public int update(BoardVO board);
	public List<BoardVO> getListWithPaging(Criterial cri);
	public int getTotalCount(Criterial cri);
	
	public void updateReplyCnt(@Param("bno") Long bno, @Param("amount") int amount);
	
	public List<BoardAttachVO> findByBno(Long bno);
	
	
	
	
	
	
	
	
	
}
