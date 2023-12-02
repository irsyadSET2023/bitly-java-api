package com.jwtexample.demo.service;

import com.jwtexample.demo.entity.UserInfo; 
import com.jwtexample.demo.repository.UserInfoRepository; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails; 
import org.springframework.security.core.userdetails.UserDetailsService; 
import org.springframework.security.core.userdetails.UsernameNotFoundException; 
import org.springframework.security.crypto.password.PasswordEncoder; 
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Optional; 

@Service
public class UserInfoService implements UserDetailsService { 

	@Autowired
	private UserInfoRepository repository; 

	@Autowired
	private PasswordEncoder encoder; 


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { 

		Optional<UserInfo> userDetail = repository.findByName(username); 

		// Converting userDetail to UserDetails 
		return userDetail.map(UserInfoDetails::new) 
				.orElseThrow(() -> new UsernameNotFoundException("User not found " + username)); 
	} 

	public String addUser(UserInfo userInfo) { 
		userInfo.setPassword(encoder.encode(userInfo.getPassword())); 
		userInfo.setCreatedAt(LocalDateTime.now());
		userInfo.setUpdatedAt(LocalDateTime.now());
		repository.save(userInfo); 
		return "User Added Successfully"; 
	} 

	public UserInfo registerUser(UserInfo userInfo) { 
		userInfo.setPassword(encoder.encode(userInfo.getPassword())); 
		userInfo.setRoles("ROLE_USER");
		userInfo.setCreatedAt(LocalDateTime.now());
		userInfo.setUpdatedAt(LocalDateTime.now());
		repository.save(userInfo); 
		return userInfo; 
	} 

} 

