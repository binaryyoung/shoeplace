package com.shoeplace.common;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class MailComponentTest {

	@InjectMocks
	MailComponent mailComponent;

	@Mock
	JavaMailSender mailSender;

	@Test
	void sendMailTest() throws Exception {
		//given
		String to = "test@test.com";
		String subject = "subject";
		String text = "text";
		MimeMessage mimeMessage = new MimeMessage((Session)null);
		
		when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

		//when
		mailComponent.sendMail(to, subject, text);

		//then
		assertEquals(to, mimeMessage.getRecipients(Message.RecipientType.TO)[0].toString());
	}
}