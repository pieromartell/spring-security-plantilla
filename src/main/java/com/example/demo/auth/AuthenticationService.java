package com.example.demo.auth;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.config.JwtService;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.IUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService  {
	
	private final IUserRepository repository;
	
	private final PasswordEncoder passwordEncoder;
	
	private final JwtService jwtservice;
	
	private final AuthenticationManager manager;
	
	
	public AuthenticationResponse register(RegisterRequest request) {
		// TODO Auto-generated method stub
		

		var user = User.builder()
				.idcliente(request.getIdCliente())
				.username(request.getUsername())
				.name(request.getName())
				.email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword()))
				.estado(request.isEstado())
				.role(Role.USER)
				.build();
		
		repository.save(user);
		var jwtToken = jwtservice.generateToken(user);
		
		return AuthenticationResponse.builder()
				.token(jwtToken)
				.build();
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		// TODO Auto-generated method stub
		manager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
				
				);
		var user = repository.findByUsername(request.getUsername())
				.orElseThrow();
		
		var jwtToken = jwtservice.generateToken(user);
		return AuthenticationResponse.builder()
				.token(jwtToken)
				.build();
	}

}
