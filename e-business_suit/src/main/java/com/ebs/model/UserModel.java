package com.ebs.model;

import lombok.Data;

@Data
public class UserModel {
	private String userName;
	private String email;
	private String password;
	private String matchingPassword;
	private String role;
	public String getUserName() {
		return userName;
	}
	public UserModel(String userName, String email, String password, String matchingPassword, String role) {
		super();
		this.userName = userName;
		this.email = email;
		this.password = password;
		this.matchingPassword = matchingPassword;
		this.role = role;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMatchingPassword() {
		return matchingPassword;
	}
	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	
}
