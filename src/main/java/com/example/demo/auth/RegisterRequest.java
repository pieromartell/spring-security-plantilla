package com.example.demo.auth;

import org.springframework.boot.context.properties.bind.DefaultValue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

	private Long  idCliente;
	private String username;
	
	private String name;
	
	private String email;
	
	private String password;
	
	
	private boolean estado;
}
