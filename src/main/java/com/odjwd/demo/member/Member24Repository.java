package com.odjwd.demo.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Member24Repository extends JpaRepository<Member24,String>{
	public Member24 findByid(String id);
}
