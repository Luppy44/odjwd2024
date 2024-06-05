package com.odjwd.demo.board;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface Board2024Repository extends JpaRepository<Board2024,Long>{
	public Board2024 findById(long boardid);
	
	//최신글 10개 
	@Query(value="SELECT * FROM (SELECT * FROM board2024 order by boardid desc) where ROWNUM<=10" ,nativeQuery=true)
	List<Board2024> findRecent();

	//지역코드별 게시판리스트
	@Query(value="SELECT * FROM Board2024 where areacode=? order by boardid desc" ,nativeQuery=true)
	public Page<Board2024> findByAreacode(String AreaCode, Pageable pageable);

	//지역코드별 제목에서 검색
	@Query(value="SELECT * FROM Board2024 WHERE areacode=?1 AND title LIKE '%'||?2||'%' order by boardid desc" ,nativeQuery=true)
	public Page<Board2024> searchBySubject(String areaCode, String text, Pageable pageable);
	
	//지역코드별 내용에서 검색
	@Query(value="SELECT * FROM Board2024 WHERE areacode=?1 AND content LIKE '%'||?2||'%' order by boardid desc" ,nativeQuery=true)
	public Page<Board2024> searchByContent(String areaCode, String text, Pageable pageable);
	
	//지역코드별 작성자 이름에서 검색
	@Query(value="SELECT * FROM Board2024 WHERE areacode=?1 AND name LIKE '%'||?2||'%' order by boardid desc" ,nativeQuery=true)
	public Page<Board2024> searchByName(String areaCode, String text, Pageable pageable);
}
