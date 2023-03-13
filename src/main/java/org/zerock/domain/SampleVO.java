package org.zerock.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor //디폴트 생성자
@AllArgsConstructor
@Data

public class SampleVO {
	
	private Integer mno;
	private String firstName;
	private String lastName;
	
	

}
