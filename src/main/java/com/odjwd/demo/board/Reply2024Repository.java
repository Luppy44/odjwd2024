package com.odjwd.demo.board;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;


@Repository
public interface Reply2024Repository extends JpaRepository<Reply2024,Long>{
	public Reply2024 findById(long replyid);
	
	@Modifying
	@Query(value="SELECT * from Reply2024 where boardid=? order by replyid desc" ,nativeQuery=true)
	List<Map<Long, Object>> findByBoardId(long boardid);
	
	// 글 삭제 시 포함된 댓글을 모두 삭제
	@Transactional
	@Modifying
	@Query(value="DELETE from Reply2024 where boardid=?" ,nativeQuery=true)
	public void deleteByBoardId(long boardid);
	
}
