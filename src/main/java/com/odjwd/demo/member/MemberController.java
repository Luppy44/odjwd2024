package com.odjwd.demo.member;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {
private static final Logger log=LoggerFactory.getLogger(MemberController.class);
	
	@Autowired
	Member24Repository repository;
	
	/* add-member 회원가입 */
	@GetMapping("/members/add-member")
	public ModelAndView member(@ModelAttribute("memberData") Member24 member, ModelAndView mav) {
		mav.setViewName("members/add-member");
		return mav;
	}
	
	@PostMapping(value="/members/add-member")
	@Transactional(readOnly=false)
	public String addMember(@Valid @ModelAttribute("memberData") Member24 member, Errors errors) {
		
		if(errors.hasErrors()) {		
			return "members/add-member";
		}
		
		/* 아이디 중복 검사 */
		String id=member.getId();
		Optional<Member24> data=repository.findById((String)id);
		
		if(data.isEmpty()) {
			repository.saveAndFlush(member);
			return "/alert/joinAlert";
		}else {
			return "/alert/fJoinAlert";			
		}
	}
	
	/* login 로그인 */
	@GetMapping("/members/login")
	public ModelAndView toLogin(@ModelAttribute("loginData") Member24 member, ModelAndView mav) {
		mav.setViewName("members/login");
		return mav;
	}
	
	@PostMapping(value="/members/login")
	@Transactional(readOnly=false)
	public ModelAndView login(@ModelAttribute("loginData") Member24 member, ModelAndView mav, HttpServletRequest request, HttpSession session) {
		String id = member.getId();  //파라미터에서 입력한 id값
		String pw = member.getPassword(); //파라미터에서 입력한 password값
		//log.info("login11111111111111 : "+id+" pass: "+pw); 잘 넘어왔는지 확인
		
		//log.info("login2222222222222222 : "+repository.findById((String)member.getId()));
		
		Optional<Member24> data=repository.findById((String)id);
		
		//log.info("login22222222222222111"+data);
		if(data.isEmpty()) {
			mav.setViewName("members/login");
			mav.addObject("idError", "존재하지 않는 아이디입니다!");
			return mav;
		}
		String dbId = data.get().getId();  //db에서 가져온 id값
		String dbPw = data.get().getPassword(); //db에서 가져온 password값
		String dbName = data.get().getName(); //db에서 가져온 password값
		//log.info("repository11111111111111 :"+data.get().getId());
		
		session = request.getSession();
		if(dbId !="" || dbId != null) {
			if(pw.equals(dbPw)) {
				mav.setViewName("main");
				session.setAttribute("sId", id);
				session.setAttribute("sName", dbName);
				return mav;				
			}else {
				mav.setViewName("members/login");
				mav.addObject("pwError", "비밀번호가 틀렸습니다!");
				return mav;								
			}
		}else {
			mav.setViewName("members/login");
			mav.addObject("idError", "존재하지 않는 아이디입니다!");
			return mav;										
		}
	}
	
	/* logout */
	@GetMapping("/logout")
	public String logout(HttpSession session){
	   session.invalidate();
	   
	   //session.removeAttribute(HttpSessionUtils.USER_SESSION_KEY);
	   return "redirect:/members/login";
	}
	
	/* 회원정보 수정 */
	@GetMapping(value="/members/update-member/{id}")
	public ModelAndView updateMem(@PathVariable("id") String id, Member24 member, 
									ModelAndView mav, HttpSession session) {
		
		// 보안성1 - 로그아웃 상태에서 다른 유저의 id 입력 시 정보를 가져오는 것을 방지
		if(session.getAttribute("sId")==null) {
			mav.setViewName("/alert/loginAlert");
			return mav;			
		}
		// 보안성2 - 로그인 상태에서 다른 유저의 id 입력 시 정보를 가져오는 것을 방지
		if(!(session.getAttribute("sId").equals(id))) {
			mav.setViewName("/alert/loginAlert2");
			return mav;			
		}
		
		mav.setViewName("members/update-member");
		Member24 data=repository.findByid((String)id);
		mav.addObject("memberData", data);
		return mav;
	}
	@PostMapping(value="/members/update-member")
	@Transactional(readOnly=false)
	public String updateMember(@Valid @ModelAttribute("memberData") Member24 member, Errors errors) {
		
		if(errors.hasErrors()) {		
			return "members/update-member";
		}
		repository.saveAndFlush(member);
		return "/alert/upMemberAlert";
	}
	
	/* 회원정보 탈퇴 */
	@GetMapping(value="/members/deleteMember/{id}")
	public ModelAndView deleteMember(@PathVariable("id") String id, Member24 member, ModelAndView mav) {
		repository.deleteById((String)id);
		return new ModelAndView("/alert/delMemberAlert");
	}

}