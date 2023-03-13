package org.zerock.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.domain.SampleVO;
import org.zerock.domain.Ticket;

import lombok.extern.log4j.Log4j;

@Log4j
@RestController // @RestController = @ResponseBody + @Controller
@RequestMapping("/sample")
public class SampleController {
	// 클라이언트한테 돌려줄 때 평문을 보내고 타입은 utf-8로(utf-8 안쓰면 한글이 깨짐)
	// text/plain = TEXT_PLAIN_VALUE
	@GetMapping(value = "/getText", produces = "text/plain; charset=utf-8")
	public String getText() {

		log.info("Mime type : " + MediaType.TEXT_PLAIN_VALUE);

		return "안녕, 이제는 안녕";

	}

	@GetMapping(value = "/getSample", produces = { MediaType.APPLICATION_JSON_VALUE, // JSON, XML타입 두개로 생성
			MediaType.APPLICATION_XML_VALUE })

	public SampleVO getSample() {
		return new SampleVO(112, "스타", "로드");
	}

	@GetMapping(value = "/getList")
	public List<SampleVO> getList() {
		
		List<SampleVO> list = new ArrayList<SampleVO> ();
		for(int i = 10; i<=20; i++)
			list.add(new SampleVO(i,i+"First", i+"Last"));
		return list;
		
//		return IntStream.range(1, 10).mapToObj(i -> new SampleVO(i, i + "First", i + "Last"))
//				.collect(Collectors.toList());
	
	}

	
	@GetMapping("/getMap")
	public Map<String, SampleVO> getMap() {
		Map<String, SampleVO> map = new HashMap<String, SampleVO> ();
		map.put("first",new SampleVO(111,"그루트","주니어"));
		return map;
	}
	
	@GetMapping(value = "/check", params = {"height","weight"})
	public ResponseEntity<SampleVO> check(Double height, Double weight) {
		//ResponseEntity 정상적인 데이터인지 비정상적인 데이터인지 
		//check()는 height와 weight를 파라미터로 반드시 전달 받아야 함
		SampleVO vo = new SampleVO(0,""+height, ""+weight); //실수(정수)를 문자열로 인식하게끔 하게 하는 것
		ResponseEntity<SampleVO> result = null;
		
		if(height <150) 
			result = ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(vo);
		else
			result = ResponseEntity.status(HttpStatus.OK).body(vo);
		
		return result;
	}
	
	@GetMapping("/product/{cat}/{pig}") //{}이용해서 변수명을 지정하고 
	public String[] getPath(
		@PathVariable("cat") String cat, //어노테이션 사용해 지정된 이름의 변숫값을 얻을 수 있음
		@PathVariable("pig") String pig) {
			
			return new String[] {"category" + cat, "productid" + pig};
		}
	
	@PostMapping("/ticket") //post방식 사용하는 이유 @RequestBody 요청한 자체를 보여주기 때문
	public Ticket convery(@RequestBody Ticket ticket) {
		return ticket;
		//객체를 반환 -> json타입으로 변환시켜서 반환
	}
	
}
