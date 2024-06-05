package com.odjwd.demo.board;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BoardController {
private static final Logger log=LoggerFactory.getLogger(BoardController.class);

	@Autowired
	Board2024Repository repository;
	@Autowired
	Reply2024Repository repository2;
	
	/* add-board 글등록 */
	@GetMapping(value="/board/add-board")
	public ModelAndView add(@ModelAttribute("boardData") Board2024 board, ModelAndView mav) { 
		mav.setViewName("board/add-board");
		return mav;
	}
	
	@PostMapping(value="/board/add-board")
	@Transactional(readOnly=false)
	public String boardAdd(@Valid @ModelAttribute("boardData") Board2024 board, Errors errors,
			@RequestParam("file") MultipartFile file) throws Exception{ 
		//log.info("error : 9999999999999999999999999999999"+file);
		
		if(errors.hasErrors()) {		
			return "board/add-board";
		}
		
		//log.info("original:7777777777777"+file.getOriginalFilename());
		
		if(file.getOriginalFilename()!="") {
			UUID uuid=UUID.randomUUID();
			String fileName=uuid+"_"+file.getOriginalFilename();
			String projectPath = System.getProperty("user.dir")+"\\src\\main\\resources\\static\\upload-img"; 
			File saveFile = new File(projectPath, fileName);
			file.transferTo(saveFile);
			LocalDate now=LocalDate.now();
			board.setTimestamp(now);
			board.setFilename(fileName);
			board.setFilepath(projectPath);
			repository.saveAndFlush(board);
			
			return "redirect:/alert/successAdd/"+board.getBoardid()+'/'+board.getAreacode();
		}else{
			LocalDate now=LocalDate.now();
			board.setTimestamp(now);
			repository.saveAndFlush(board);			
			return "redirect:/alert/successAdd/"+board.getBoardid()+'/'+board.getAreacode();
		}	
	}
	@GetMapping(value="/alert/successAdd/{boardid}/{areacode}")
	public ModelAndView success(@PathVariable("boardid") long boardid, @PathVariable("areacode") String areacode, ModelAndView mav) { 
		mav.setViewName("alert/successAdd");
		mav.addObject("boardid", boardid);
		mav.addObject("areacode", areacode);
		return mav;
	}
	
	/* board-list 글목록 */
	@GetMapping("/board/board-list/{areacode}")
	public ModelAndView list(@PathVariable("areacode") String Areacode, Board2024 board, 
			@PageableDefault(page=0, size=10) Pageable pageable, ModelAndView mav) {
		mav.setViewName("board/board-list");
		//log.info("err1111111111111");
		//게시판 제목
		if(Areacode.equals("11")) {
			mav.addObject("board_tit","부산 진구");
			mav.addObject("board_areacode","11");
		}
		if(Areacode.equals("12")) {
			mav.addObject("board_tit","부산 수영구");
			mav.addObject("board_areacode","12");
		}
		if(Areacode.equals("13")) {
			mav.addObject("board_tit","부산 해운대구");
			mav.addObject("board_areacode","13");
		}
		if(Areacode.equals("14")) {
			mav.addObject("board_tit","부산 동래구");
			mav.addObject("board_areacode","14");
		}
		if(Areacode.equals("15")) {
			mav.addObject("board_tit","부산 사상구");
			mav.addObject("board_areacode","15");
		}
		
		//areacode 기준으로 DB에서 가져옴
		Page <Board2024> list=repository.findByAreacode((String) Areacode, pageable);
		
		//page처리
		int nowPage=list.getPageable().getPageNumber()+1;
		int startPage=Math.max(nowPage-4, 1);
		int endPage=Math.min(nowPage+9, list.getTotalPages());
		int lastPage=Math.max(nowPage,list.getTotalPages());
		//log.info("list77777777777777777:"+list);
		mav.addObject("boards", list);
		mav.addObject("nowPage", nowPage);
		mav.addObject("startPage", startPage);
		mav.addObject("endPage", endPage);
		mav.addObject("lastPage", lastPage);
		return mav;
	}
	
	/* view-board 글보기 */
	@GetMapping(value="/board/view-board/{boardid}")
	public ModelAndView view(@PathVariable("boardid") int boardid, @ModelAttribute Board2024 board, @ModelAttribute Reply2024 reply, ModelAndView mav) { 
		Board2024 data=repository.findById((long)boardid);
		int uphit=data.getHit()+1;
		data.setHit(uphit);
		//log.info("error2222222222222222222222222222222222222222222222 : "+data);
		repository.saveAndFlush(data);
		Board2024 data1=repository.findById((long)boardid);
		List<Map<Long, Object>> list=repository2.findByBoardId((long)boardid);
		//log.info("error22222222222222222222222222222222222222222222223333333333 : "+data1);
		mav.setViewName("board/view-board");
		mav.addObject("boardData", data1);
		mav.addObject("replyData", list);
		//log.info("data1:7777777777777777777777777777777"+data1);
		return mav;
	}
	
	/* 글 수정 */
	@GetMapping(value="/board/updateBoard/{boardid}")
	public ModelAndView update(@PathVariable("boardid") int boardid, Board2024 board, ModelAndView mav) {
		mav.setViewName("board/updateBoard");
		Board2024 data=repository.findById((long)boardid);
		mav.addObject("boardData", data);
		return mav;
	}
	@PostMapping(value="/board/updateBoard")
	@Transactional(readOnly=false)
	public String updateBoard(@Valid @ModelAttribute("boardData") Board2024 board, Errors errors,
			@RequestParam("file") MultipartFile file) throws Exception {
		//log.info("data1:7777777777777777777777777777777"+file.getOriginalFilename());
		
		if(errors.hasErrors()) {		
			return "board/updateBoard";
		}
		
		
		if(file.getOriginalFilename()!="") {
			UUID uuid=UUID.randomUUID();
			String fileName=uuid+"_"+file.getOriginalFilename();
			String projectPath = System.getProperty("user.dir")+"\\src\\main\\resources\\static\\upload-img"; 
			File saveFile = new File(projectPath, fileName);
			file.transferTo(saveFile);
			
			LocalDate now=LocalDate.now();
			board.setTimestamp(now);
			board.setFilename(fileName);
			board.setFilepath(projectPath);
			repository.saveAndFlush(board);
			
			return "redirect:/board/view-board/"+board.getBoardid();
		}else{
			Board2024 data=repository.findById((long) board.getBoardid());
			LocalDate now=LocalDate.now();
			board.setTimestamp(now);
			board.setFilename(data.getFilename());
			board.setFilepath(data.getFilepath());
			repository.saveAndFlush(board);
			
			return "redirect:/board/view-board/"+board.getBoardid();
		}
		
	}
	/* 글 삭제 */
	
	@GetMapping(value="/board/deleteBoard/{boardid}/{areacode}")
	public ModelAndView deleteBoard(@PathVariable("boardid") long boardid, 
									@PathVariable("areacode") String areacode, ModelAndView mav) {
		
		//게시글을 지우면 포함된 댓글도 모두 삭제!!
		repository.deleteById((long)boardid);
		repository2.deleteByBoardId((long)boardid);
		
		return new ModelAndView("redirect:/board/board-list/"+areacode);
	}
	
	/* 댓글 작성 */
	@PostMapping(value="/board/reply")
	@Transactional(readOnly=false)
	public ModelAndView replyAdd(@ModelAttribute("replyData") Reply2024 reply, Board2024 board, ModelAndView mav) { 
		
		repository2.saveAndFlush(reply);
	
		//댓글수 업데이트
		Board2024 data=repository.findById((long)reply.getBoardid());
		//log.info("data777777777777777777777777777 : "+data);
		data.setRecnt(data.getRecnt()+1);
		//log.info("data777777777777777777777777777 : "+data);
		repository.saveAndFlush(data);
		
		mav.setViewName("redirect:/board/view-board/"+reply.getBoardid());
		return mav;
	}
	
	/* 댓글 수정 */
	@PostMapping(value="/board/updateReply")
	@Transactional(readOnly=false)
	public String updateReply(@ModelAttribute("replyUpdateData") Reply2024 reply) { 
		repository2.saveAndFlush(reply);
		//log.info("boardid : 777777777777777777777777777"+reply.getBoardid());
		return "redirect:/board/view-board/"+reply.getBoardid();
	}
	
	/* 댓글 삭제 */
	@GetMapping(value="/board/deleteReply/{replyid}/{boardid}")
	public ModelAndView deleteReply(@PathVariable("replyid") long replyid,
			@PathVariable("boardid") long boardid, Reply2024 reply,Board2024 board, ModelAndView mav) {
		repository2.deleteById((long)replyid);
		
		//댓글수 업데이트
			Board2024 data=repository.findById((long)reply.getBoardid());
			data.setRecnt(data.getRecnt()-1);
			repository.saveAndFlush(data);
				
		//log.info("error : 09999999999999999999999999999999");
		return new ModelAndView("redirect:/board/view-board/"+boardid);
	}
	
	/* 찾았어요 */
	@GetMapping(value="/board/complete/{boardid}")
	public ModelAndView complete(@PathVariable("boardid") long boardid, Board2024 board, ModelAndView mav) {
		
		Board2024 data=repository.findById((long)boardid);
		data.setComplete(1);
		//log.info("data77777777777777777777777777 : " +data);
		repository.saveAndFlush(data);
		
		return new ModelAndView("redirect:/board/view-board/"+boardid);
	}
	
	/* 찾았어요 취소 */
	@GetMapping(value="/board/cancleComplete/{boardid}")
	public ModelAndView cancleComplete(@PathVariable("boardid") long boardid, Board2024 board, ModelAndView mav) {
		
		Board2024 data=repository.findById((long)boardid);
		data.setComplete(0);
		//log.info("data77777777777777777777777777 : " +data);
		repository.saveAndFlush(data);
		
		return new ModelAndView("redirect:/board/view-board/"+boardid);
	}
	
	/* 검색 */
	@GetMapping(value="/board/search-list")
	public ModelAndView search(HttpServletRequest request, 
								@PageableDefault(page=0, size=10) Pageable pageable, ModelAndView mav) { 
		
		String items=request.getParameter("items");
		String text=request.getParameter("text");
		String areacode=request.getParameter("areacode");
		
		//에러 반환
		if(text=="") {
			mav.addObject("searchError","검색어를 입력하세요!!");
		}
		
		//게시판 제목
		if(areacode.equals("11")) {
			mav.addObject("board_tit","부산 진구");
			mav.addObject("board_areacode","11");
		}
		if(areacode.equals("12")) {
			mav.addObject("board_tit","부산 수영구");
			mav.addObject("board_areacode","12");
		}
		if(areacode.equals("13")) {
			mav.addObject("board_tit","부산 해운대구");
			mav.addObject("board_areacode","13");
		}
		if(areacode.equals("14")) {
			mav.addObject("board_tit","부산 동래구");
			mav.addObject("board_areacode","14");
		}
		if(areacode.equals("15")) {
			mav.addObject("board_tit","부산 사상구");
			mav.addObject("board_areacode","15");
		}
		
		// 검색 키워드별
		Page <Board2024> list=null;
		if(items.equals("subject")) {
			list=repository.searchBySubject((String) areacode, (String) text, pageable);
		}
		if(items.equals("content")) {
			list=repository.searchByContent((String) areacode, (String) text, pageable);
		}
		if(items.equals("name")) {
			list=repository.searchByName((String) areacode, (String) text, pageable);
		}
		
		int nowPage=list.getPageable().getPageNumber()+1;
		int startPage=Math.max(nowPage-4, 1);
		int endPage=Math.min(nowPage+9, list.getTotalPages());
		int lastPage=Math.max(nowPage,list.getTotalPages());
		
		mav.setViewName("board/search-list");
		mav.addObject("boards", list);
		mav.addObject("nowPage", nowPage);
		mav.addObject("startPage", startPage);
		mav.addObject("endPage", endPage);
		mav.addObject("lastPage", lastPage);
		
		return mav;
	}
	
}
