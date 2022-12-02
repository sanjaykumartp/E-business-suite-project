package com.ebs.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/*
 * 2/12/2022
 * Note:
 * 	1- All access working fine
 *  2- Registering user using admin credential
 *  
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration { 

	//Its a default Spring Security Interface uses to do default Authentication
	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(11);
	}

	@Bean
	AuthenticationProvider authenticationProvider() {
		//DAO= Data Access Object
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(new BCryptPasswordEncoder());
		return provider;
	}
	private static final String[] WHITE_LIST_URLS = {
			"/ebs/wc",
			"/ebs/home"	
	};
	private static final String[] USER_LIST_URLS = {
			"/ebs/user",
			"/ebs/user/{userName}"
	};
	private static final String[] ADMIN_LIST_URLS = {
			"/ebs/**"
	};
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
	{
		http.csrf().disable()
		.authorizeRequests()
		.antMatchers(WHITE_LIST_URLS).permitAll()
		.antMatchers(USER_LIST_URLS).access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
		.antMatchers(ADMIN_LIST_URLS).access("hasRole('ROLE_ADMIN')")
		.anyRequest()
		.authenticated()
		.and()
		.httpBasic();
		return http.build();
	}

}
