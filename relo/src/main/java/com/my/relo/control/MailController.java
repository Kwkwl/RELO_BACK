package com.my.relo.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.my.relo.dto.MemberDTO;
import com.my.relo.service.EmailServiceImpl;
import com.my.relo.service.MemberService;

@RestController
@RequestMapping("member/*")
public class MailController {

	@Autowired
	private EmailServiceImpl emailService;
	
	@Autowired
	private MemberService ms;

	@PostMapping("findpwd/emailconfig")
	public void emailConfirm(String email, String tel) throws Exception {

		String confirm = emailService.sendSimpleMessage(email);
		
		Long mNum = emailService.findMNum(tel);
		MemberDTO dto = MemberDTO.builder().pwd(confirm).build();
		
		ms.updateProfile(mNum, dto);
	}

}