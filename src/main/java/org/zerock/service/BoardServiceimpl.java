package org.zerock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.domain.BoardAttachVO;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criterial;
import org.zerock.mapper.BoardAttachMapper;
import org.zerock.mapper.BoardMapper;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class BoardServiceimpl implements BoardService {

	@Setter(onMethod_=@Autowired)
	private BoardMapper boardMapper;
	
	@Setter(onMethod_=@Autowired)
	private BoardAttachMapper attachMapper;
	
	@Transactional
	@Override
	public void register(BoardVO board) {
		
		log.info(board);
		
		boardMapper.insertSelectKey(board);
		
		if (board.getAttachList() == null || board.getAttachList().size() <= 0) {
			return;
		}

		board.getAttachList().forEach(attach->{

			attach.setBno(board.getBno());
			attachMapper.insert(attach);
		});
	}

	@Override
	public BoardVO get(Long bno) {
		return boardMapper.read(bno);
	}

	@Override
	public boolean modify(BoardVO vo) {
		
		log.info("modify.." + vo );
		attachMapper.deleteAll(vo.getBno());
		boolean modifyResult = boardMapper.update(vo) == 1;
		
		if(modifyResult && vo.getAttachList() != null && vo.getAttachList().size() > 0) {
			vo.getAttachList().forEach(attach -> {
				
				attach.setBno(vo.getBno());
				attachMapper.insert(attach);
				
			});
			
		}
		
		return modifyResult;
	}

	@Override
	public boolean remove(Long bno) {
		
		log.info("remove.." +bno);
		attachMapper.deleteAll(bno);
		return boardMapper.delete(bno)==1;
				//? true : false;
	}

	@Override
	public List<BoardVO> getList(Criterial cri) {
		return boardMapper.getListWithPaging(cri);
	}
	
	@Override
	public int getTotal(Criterial cri) {
		return boardMapper.getTotalCount(cri);
	}

	@Override
	public List<BoardAttachVO> getAttachList(Long bno) {
		//게시물의 첨부파일들의 목록 가져오기
		log.info("get Attach list by bno" + bno);
		return attachMapper.findByBno(bno);
	}
	

}
