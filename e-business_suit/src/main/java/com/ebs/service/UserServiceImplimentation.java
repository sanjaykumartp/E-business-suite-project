package com.ebs.service;

import java.util.Calendar;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ebs.custom.service.CustomUserDetails;
import com.ebs.entity.PasswordResetToken;
import com.ebs.entity.User;
import com.ebs.entity.VerificationToken;
import com.ebs.model.UserModel;
import com.ebs.repository.PasswordResetTokenRepository;
import com.ebs.repository.UserRepository;
import com.ebs.repository.VerificationTokenRepository;

@Service
public class UserServiceImplimentation implements UserService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	private VerificationTokenRepository verificationTokenRepository;
	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;
	@Autowired
	private JavaMailSender mailSender;
	
	@Override
	public User register(UserModel userModel) {
		System.out.println("Register Method Calling");
		User user = new User();
		if (!(userRepository.findByEmail(userModel.getEmail())==null)) {
			throw new DuplicateKeyException("Email ID Already Exsists");
		}else if (!(userRepository.findByUserName(userModel.getUserName())==null)) {
			throw new DuplicateKeyException("UserName Already Exsists");
		}else {
			user.setEmail(userModel.getEmail().toLowerCase());
			user.setUserName(userModel.getUserName().toLowerCase());
			user.setRole("ROLE_"+userModel.getRole().toUpperCase());
			if (userModel.getPassword().equals(userModel.getMatchingPassword())) {
				user.setPassword(passwordEncoder.encode(userModel.getPassword()));
			}else {
				throw new InputMismatchException();
			}
//			user.setPassword(passwordEncoder.encode(userModel.getPassword()));
			userRepository.save(user);
			
		}
		return user;
	}
	
	//user and token both are saved to the database
		@Override
		public void saveVerificationTokenForUser(String token, User user) {
			VerificationToken verificationToken
			= new VerificationToken(user, token);

			verificationTokenRepository.save(verificationToken);
		}
		@Override
		public void sendSimpleEmail(String toEmail,
				String body,
				String subject) {
			SimpleMailMessage message = new SimpleMailMessage();

			message.setFrom("sanjay.kumar@estuate.com");
			message.setTo(toEmail);
			message.setText(subject);
			message.setSubject(body);

			mailSender.send(message);
			System.out.println("Mail Send...");
		}

		@Override
		public String validateVerificationToken(String token) {
			VerificationToken verificationToken
			= verificationTokenRepository.findByToken(token);

			if (verificationToken == null) {
				return "invalid";
			}

			/*
			 * here we check link expiration time
			 */
			User user = verificationToken.getUser();
			Calendar cal = Calendar.getInstance();

			if ((verificationToken.getExpirationTime().getTime()
					- cal.getTime().getTime()) <= 0) {
				verificationTokenRepository.delete(verificationToken);
				return "expired";
			}

			user.setEnabled(true);
			userRepository.save(user);
			return "valid";
		}

		@Override
		public VerificationToken generateNewVerificationToken(String oldToken) {
			VerificationToken verificationToken
			= verificationTokenRepository.findByToken(oldToken);
			verificationToken.setToken(UUID.randomUUID().toString());
			verificationTokenRepository.save(verificationToken);
			return verificationToken;
		}

		@Override
		public User findUserByEmail(String email) {
			return userRepository.findByEmail(email);
		}

		@Override
		public void createPasswordResetTokenForUser(User user, String token) {
			PasswordResetToken passwordResetToken
			= new PasswordResetToken(user,token);
			passwordResetTokenRepository.save(passwordResetToken);
		}

		@Override
		public String validatePasswordResetToken(String token) {
			PasswordResetToken passwordResetToken
			= passwordResetTokenRepository.findByToken(token);

			if (passwordResetToken == null) {
				return "invalid";
			}

			User user = passwordResetToken.getUser();
			Calendar cal = Calendar.getInstance();

			if ((passwordResetToken.getExpirationTime().getTime()
					- cal.getTime().getTime()) <= 0) {
				passwordResetTokenRepository.delete(passwordResetToken);
				return "expired";
			}

			return "valid";
		}

		@Override
		public Optional<User> getUserByPasswordResetToken(String token) {
			return Optional.ofNullable(passwordResetTokenRepository.findByToken(token).getUser());
		}

		@Override
		public void changePassword(User user, String newPassword) {
			user.setPassword(passwordEncoder.encode(newPassword));
			userRepository.save(user);
		}

		@Override
		public boolean checkIfValidOldPassword(User user, String oldPassword) {
			return passwordEncoder.matches(oldPassword, user.getPassword());
		}




	@Override
	public User getUserByUserName(String userName) {
		User user = userRepository.findByUserName(userName);
		return user;
	}


	

	



}
