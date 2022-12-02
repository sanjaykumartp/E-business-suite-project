package com.ebs.event.listener;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.ebs.entity.User;
import com.ebs.event.RegistrationCompleteEvent;
import com.ebs.service.UserService;

import java.util.UUID;

@Component
@Slf4j
public class RegistrationCompleteEventListener implements
ApplicationListener<RegistrationCompleteEvent> {


	@Autowired
	private UserService userService;

	//	@EventListener(ApplicationReadyEvent.class)
	@Override
	public void onApplicationEvent(RegistrationCompleteEvent event) {
		//Create the Verification Token for the User with Link
		User user = event.getUser();
		String token = UUID.randomUUID().toString();
		userService.saveVerificationTokenForUser(token,user);

		//Send Mail to user
		//so here just we display the mail in the console we
		//are not sending the msg through mail

		//this string consist of hole token address



		String url =event.getApplicationUrl()
				+ "/verifyRegistration?token="
				+ token;

		userService.sendSimpleEmail("sanjay.kumar@estuate.com", "email verification", url);


		//sendVerificationEmail()
		log.info("Click the link to verify your account: {}",
				url);
	}


}
