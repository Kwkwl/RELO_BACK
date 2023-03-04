package com.my.relo.service;

import java.util.Random;

import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.my.relo.entity.Member;
import com.my.relo.repository.MemberRepository;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	JavaMailSender emailSender;
	
	@Autowired
	private MemberRepository mr;

	public Long findMNum(String tel) {
		Member m = mr.findIdAndPwd(tel);
		return m.getMNum();
	}

	private MimeMessage createMessage(String to) throws Exception {
		String ePw = createKey();
		System.out.println("보내는 대상 : " + to);
		System.out.println("인증 번호 : " + ePw);
		MimeMessage message = emailSender.createMimeMessage();

		message.addRecipients(RecipientType.TO, to);// 보내는 대상
		message.setSubject("[RELO] 비밀번호 재설정 안내입니다.");// 제목

		String msgg = "";
		msgg += "<div align='center'>";
		msgg += "<h3> 안녕하세요 RELO입니다.<br/> 저희 서비스 이용을 위한 로그인 관련 메일입니다.</h3>";
		msgg += "<br>";
		msgg += "<p>RELO 회원님의 임시비밀번호 입니다.<p>";
		msgg += "<br>";
		msgg += "<br>";
		msgg += "<div align='center' style='font-family:verdana';>";
		msgg += "<h3 style='color:blue;'>임시 비밀번호</h3>";
		msgg += "<div style='font-size:130%'>";
		msgg += "CODE : <strong>";
		msgg += ePw + "</strong><div><br/> ";
		msgg += "</div>";
		msgg += "<p> *본 메일은 발신 전용입니다. 더 궁금하신 내용은 고객센터로 문의주시면 신속하게 답변드리겠습니다.</p>";
		msgg += "<p> *고객센터 : 1234-5678(평일 11:00 - 18:00)</p>";

		String code = ePw;
		message.setDescription(code);
		message.setText(msgg, "UTF-8", "HTML");// 내용
		message.setFrom(new InternetAddress("relokosta@gmail.com", "relokosta"));// 보내는 사람
		return message;
	}

	public String createKey() {
		StringBuffer key = new StringBuffer();
		Random rnd = new Random();

		for (int i = 0; i < 8; i++) { // 인증코드 8자리
			int index = rnd.nextInt(3); // 0~2 까지 랜덤

			switch (index) {
			case 0:
				key.append((char) ((int) (rnd.nextInt(26)) + 97));
				// a~z (ex. 1+97=98 => (char)98 = 'b')
				break;
			case 1:
				key.append((char) ((int) (rnd.nextInt(26)) + 65));
				// A~Z
				break;
			case 2:
				key.append((rnd.nextInt(10)));
				// 0~9
				break;
			}
		}
		return key.toString();
	}

	@Override
	public String sendSimpleMessage(String to) throws Exception {
		// TODO Auto-generated method stub
		MimeMessage message = createMessage(to);
		try {// 예외처리
			emailSender.send(message);
			String msg = (String) message.getDescription();
			return msg;
		} catch (MailException es) {
			es.printStackTrace();
			throw new IllegalArgumentException();
		}
	}
}