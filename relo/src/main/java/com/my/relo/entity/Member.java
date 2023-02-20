package com.my.relo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "member")
@SequenceGenerator(name = "member_sequence_generator", // 제너레이터명
sequenceName = "member_seq", // 시퀀스명
initialValue = 1, // 시작 값
allocationSize = 1 // 할당할 범위 사이즈
)
public class Member {
	
	/**
	 * 회원 번호
	 */
	@Id
	@Column(name = "m_num")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_sequence_generator")
	private Long mNum; 

	private String id; 

	@Column(nullable = false)
	private String pwd; 

	@Column(nullable = false)
	private String tel;

	@Column(nullable = false)
	private String email;
	
	/**
	 * 유형 - 관리자(1) / 판매자 및 구매자(0) 
	 */
	@Column(nullable = false)
	private int type; 

	@Column(nullable = false)
	private String birth;

	@Column(nullable = false)
	private String name;
	
	/**
	 * 탈퇴 여부 -> 탈퇴시 -1이 insert됨
	 */
	@Column(name = "out_ck")
	private Integer outCk;
	
	@OneToOne(mappedBy = "member")
	private Account account;
}
