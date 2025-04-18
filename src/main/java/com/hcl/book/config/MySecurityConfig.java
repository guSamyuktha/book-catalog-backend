package com.hcl.book.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.hcl.book.filter.JwtAuthFilter;
import com.hcl.book.service.UserInfoService;

@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity
public class MySecurityConfig {	
	
	@Autowired
	JwtAuthFilter authFilter;
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		.csrf(csrf -> csrf.disable()) // Disable CSRF for stateless APIs
		.authorizeHttpRequests(auth->auth
				.requestMatchers("/auth/addNewUser", 
						"/auth/generateToken",
						"/v3/api-docs/**",    // Allow access to OpenAPI docs
						"/swagger-ui/**",     // Allow access to Swagger UI
						"/swagger-ui.html",
						"/api/v1/books").permitAll()
				.requestMatchers("/auth/user/**").hasAuthority("ROLE_USER")
				.requestMatchers("/auth/admin/**").hasAuthority("ROLE_ADMIN")
				.anyRequest().authenticated()				
				)
		.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // No sessions
		.authenticationProvider(authenticationProvider())
		.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
	
	@Bean
 	public WebSecurityCustomizer webSecurityCustomizer() {
 		return (web) -> web.ignoring()
 		// Spring Security should completely ignore URLs starting with /resources/
 				.requestMatchers("/resources/**");
 	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); // Password encoding
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserInfoService(); // Ensure UserInfoService implements UserDetailsService
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

}
