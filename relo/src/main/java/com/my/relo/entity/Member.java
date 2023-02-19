package com.my.relo.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

	private String pwd; 

	private String tel;

	private String email;
	
	/**
	 * 유형 - 관리자(1) / 판매자 및 구매자(0) 
	 */
	private int type; 

	private String birth;

	private String name;
	
	/**
	 * 탈퇴 여부 -> 탈퇴시 -1이 insert됨
	 */
	@Column(name = "out_ck")
	private Integer outCk;
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "addr_num")
	private List<Address> addresses;
	
}
