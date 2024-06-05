package com.odjwd.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.odjwd.demo.board.Board2024;
import com.odjwd.demo.board.Board2024Repository;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MainController {
	@Autowired
	Board2024Repository repository;
	
	@GetMapping("/")
	public String index() {
		log.info("!!");
		return "index";
	}
	
	@GetMapping("/main")
	public ModelAndView main(Board2024 board, ModelAndView mav) {
		List<Board2024> list=repository.findRecent();
		
		mav.setViewName("main");
		mav.addObject("boards", list);
		return mav;
	}
}
