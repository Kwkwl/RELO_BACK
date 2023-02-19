package com.my.relo.repository;

import org.springframework.data.repository.CrudRepository;

import com.my.relo.entity.Member;

public interface MemberRepository extends CrudRepository<Member, Long> {
	public Member findById(String id);
}
