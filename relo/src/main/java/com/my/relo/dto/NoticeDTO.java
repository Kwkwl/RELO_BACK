package com.my.relo.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.my.relo.entity.Notice;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NoticeDTO {

	private Long nNum;

	private Long mNum;

	private String nTitle;

	private String nContent;
	
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	private LocalDate ndate;

	/**
	 * 서비스(0) / 작업(1) / 업데이트(2) / 이벤트(3)
	 */
	private Integer nCategory;

	@Builder
	public NoticeDTO(Long nNum, Integer category, String title, String content, LocalDate date, Long mNum) {
		this.nNum = nNum;
		this.nCategory = category;
		this.nTitle = title;
		this.nContent = content;
		this.ndate = date;
		this.mNum = mNum;
	}

	public Notice toEntity() {
		Notice entity = Notice.builder().category(this.nCategory).date(this.ndate).title(this.nTitle)
				.content(this.nContent).mNum(this.mNum).build();

		return entity;
	}

}
