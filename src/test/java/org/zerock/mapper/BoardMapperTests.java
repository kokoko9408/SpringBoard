package org.zerock.mapper;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criterial;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class BoardMapperTests {
	
	@Autowired
	BoardMapper mapper;
	
	@Test
	public void TestGetList() {
		mapper.getList().forEach(n->log.info(n));
	}
	
	@Test
	public void testInsert() {
		BoardVO vo = new BoardVO();
		vo.setTitle("테스트 제목");
		vo.setContent("테스트 내용");
		vo.setWriter("테스트");
		mapper.insert(vo);
		log.info(vo);
	}
	
	@Test
	public void testInsertSelectKey() {
		BoardVO vo = new BoardVO();
		vo.setTitle("테스트 제목2");
		vo.setContent("테스트 내용2");
		vo.setWriter("테스트2");
		mapper.insertSelectKey(vo);
		log.info(vo);
	}
	
	@Test
	public void testRead() {
		BoardVO vo = mapper.read(5702882L);
		log.info(vo);
	}

	@Test
	public void testDelete() {
		log.info("delete==> " + mapper.delete(5702882L));
	}

	@Test  //5702883
	public void testUpdate() {
		BoardVO vo = new BoardVO();
		vo.setTitle("update 제목");
		vo.setContent("update 내용");
		vo.setWriter("update");
		vo.setBno(5702883L);
		log.info("update ==> " +  mapper.update(vo));
	}
	
	@Test
	public void getListWithPaging() {
		Criterial cri = new Criterial(2,15);
		cri.setType("T");
		cri.setKeyword("고칠게");
		List<BoardVO> list = mapper.getListWithPaging(cri);
		list.forEach(n->log.info(n));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
