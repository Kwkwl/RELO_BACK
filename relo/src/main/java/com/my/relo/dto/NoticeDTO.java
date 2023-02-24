package com.my.relo.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.my.relo.entity.Notice;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//@Setter
@Getter
//@NoArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class NoticeDTO {

	private Long nNum;

	private Long mNum;

	private String nTitle;

	private String nContent;

	@JsonFormat(timezone = "Asia/Seoul", pattern = "yy-MM-dd")
	private LocalDate nDate;

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
		this.nDate = date;
		this.mNum = mNum;
	}

	public Notice toEntity() {
		Notice entity = Notice.builder().nCategory(this.nCategory).nDate(this.nDate).nTitle(this.nTitle)
				.nContent(this.nContent).mNum(this.mNum).build();

		return entity;
	}

}
