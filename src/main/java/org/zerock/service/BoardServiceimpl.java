package org.zerock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criterial;
import org.zerock.mapper.BoardMapper;

@Service
public class BoardServiceimpl implements BoardService {

	@Autowired
	public BoardMapper boardMapper ;
	
	
	@Override
	public void register(BoardVO vo) {
		boardMapper.insertSelectKey(vo);
	}

	@Override
	public BoardVO get(Long bno) {
		return boardMapper.read(bno);
	}

	@Override
	public boolean modify(BoardVO vo) {
		return boardMapper.update(vo)==1? true : false;
	}

	@Override
	public boolean remove(Long bno) {
		return boardMapper.delete(bno)==1? true : false;
	}

//	@Override
//	public List<BoardVO> getList() {
//		return boardMapper.getList();
//	}
	
	@Override
	public List<BoardVO> getList(Criterial cri) {
		return boardMapper.getListWithPaging(cri);
	}
	
	@Override
	public int getTotal(Criterial cri) {
		return boardMapper.getTotalCount(cri);
	}

}
