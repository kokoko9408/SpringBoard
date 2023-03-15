package org.zerock.domain;

import org.springframework.web.util.UriComponentsBuilder;

import lombok.Data;

@Data
public class Criterial {
	
	private int pageNum;
	private int amount;
	private String type; //제목(T), 내용(C), 저자(W)
	private String keyword; //검색 키워드
	
	
	public Criterial() {
		this(1,10); //한 페이지에 1-10까지 10개 씩 보여주겠단 소리
	}
	
	public Criterial(int pageNum, int amount) {
		this.pageNum = pageNum;
		this.amount = amount;
	}
	
	public String[] getTypeArr() {
		return type == null? new String[] {} : type.split("");
		//타입이 null이면 빈 문자열 생성, null이 아니면 
		
	}
	
	public String getListLink() {
		//UriComponentsBuilder는 브라우저에서 get방식 등의 파라미터 전송에 사용되는 문자열을
		//손쉽게 처리할 수 있는 클래스
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("")
				.queryParam("pageNum", this.pageNum)
				.queryParam("amount", this.getAmount())
				.queryParam("type", this.getType())
				.queryParam("keyword", this.getKeyword());

		return builder.toUriString();

	}
	
}
