package com.ebs.service;


import java.util.Optional;

import com.ebs.entity.User;
import com.ebs.entity.VerificationToken;
import com.ebs.model.UserModel;

public interface UserService {

	User register(UserModel userModel);

	User getUserByUserName(String userName);

	void saveVerificationTokenForUser(String token, User user);

	String validateVerificationToken(String token);

	VerificationToken generateNewVerificationToken(String oldToken);

	User findUserByEmail(String email);

	void createPasswordResetTokenForUser(User user, String token);

	String validatePasswordResetToken(String token);

	Optional<User> getUserByPasswordResetToken(String token);

	void changePassword(User user, String newPassword);

	boolean checkIfValidOldPassword(User user, String oldPassword);

	void sendSimpleEmail(String toEmail, String subject, String body);


}
