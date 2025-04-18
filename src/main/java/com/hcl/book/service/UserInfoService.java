package com.hcl.book.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hcl.book.entities.UserInfo;
import com.hcl.book.entities.UserInfoDetails;
import com.hcl.book.repository.UserInfoRepository;

@Service
public class UserInfoService implements UserDetailsService{
	@Autowired
	UserInfoRepository userInfoRepository;

	@Autowired
	private PasswordEncoder encoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserInfo> userDetail = userInfoRepository.findByUsername(username); 

		// Converting UserInfo to UserDetails
		return userDetail.map(UserInfoDetails::new)// new UserInfoDetails(userDetail)
				.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
	}
//Registration of new User
	public String addUser(UserInfo userInfo) {
		// Encode password before saving the user
		userInfo.setPassword(encoder.encode(userInfo.getPassword()));
		userInfoRepository.save(userInfo);
		return "User Added Successfully";
	}


}
