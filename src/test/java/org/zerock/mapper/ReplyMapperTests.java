package org.zerock.mapper;

import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zerock.domain.Criterial;
import org.zerock.domain.ReplyVO;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class ReplyMapperTests {

	private Long[] bnoArr = {211946L, 212582L, 212583L, 212584L, 212585L};
	
	@Autowired
	private ReplyMapper mapper;

	@Test
	public void testMapper() {
		log.info(mapper);
	}

	@Test
	public void testInsert() {
//		ReplyVO vo = new ReplyVO();
//		vo.setBno(236494L);
//		vo.setReply("노래가 너무 좋아요");
//		vo.setReplyer("채수림");
//		mapper.insert(vo);
//		log.info("vo-->" + vo);
		
		IntStream.range(1, 10).forEach(i->{
			ReplyVO vo = new ReplyVO();
			vo.setBno(bnoArr[i%5]);
			vo.setReply("노래가 조타"+i);
			vo.setReplyer("채수림"+i);
			
			mapper.insert(vo);
			
		});
		
	}
	
	@Test
	public void testRead() { 
		
		ReplyVO vo = mapper.read(1L);
		log.info(vo);
		
	}

	@Test
	public void testDelete() { 
		
		log.info("vo :" + mapper.delete(1L));
		
	}
	
	@Test
	public void testUpdate() {
		ReplyVO vo = new ReplyVO();
		vo.setReply("노래가 구려요 수정");
		vo.setRno(2L);
		log.info("vo :" +mapper.update(vo));
		
	}
	
	@Test
	public void testGetListWithPaging() {
		//log.info("vo" + mapper.getListWithPaging(new Criterial(), 212582L));
		log.info("vo" + mapper.getListWithPaging(new Criterial(), bnoArr[2]));
	
	}
	
	@Test
	public void testGetListWithPaging2() {
		//log.info("vo" + mapper.getListWithPaging(new Criterial(), 212582L));
		log.info("vo" + mapper.getListWithPaging(new Criterial(), bnoArr[2]));
	
	}

	
}
