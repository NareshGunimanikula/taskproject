package com.example.taskproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskproject.payload.JWTAuthResponse;
import com.example.taskproject.payload.LoginDto;
import com.example.taskproject.payload.UserDto;
import com.example.taskproject.security.JwtToeknProvider;
import com.example.taskproject.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtToeknProvider jwtToeknProvider;
	
	//post method to store the data into DB
	@PostMapping("/register")
	public ResponseEntity<UserDto> createUSer(@RequestBody UserDto userDto) {
		return new ResponseEntity<>(userService.createUser(userDto),HttpStatus.CREATED);
	}
	
	@PostMapping("/login")
	public ResponseEntity<JWTAuthResponse> loginUser(@RequestBody LoginDto loginDto){
		Authentication authentication = 
				authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
						);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String token = jwtToeknProvider.generateToekn(authentication);
		
		return ResponseEntity.ok(new JWTAuthResponse(token));
	}
}
 