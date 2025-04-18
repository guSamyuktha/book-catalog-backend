package com.hcl.book.entities;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserInfoDetails implements UserDetails{
	
	private static final long serialVersionUID = 497835333384152074L;
	
	private String username; 
	 private String password;
	 private List<GrantedAuthority> authorities;
	 
	 public UserInfoDetails(UserInfo userInfo){
		 this.username = userInfo.getUsername();
	     this.password = userInfo.getPassword();
	     this.authorities = List.of(userInfo.getRoles().split(","))
	                .stream()
	                .map(SimpleGrantedAuthority::new)
	                .collect(Collectors.toList());
	   
	 }
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		 return authorities;
	}

	@Override
	public String getPassword() {		
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

}
