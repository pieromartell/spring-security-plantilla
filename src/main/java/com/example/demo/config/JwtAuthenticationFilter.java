package com.example.demo.config;

import java.io.IOException;

import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtservice;
	
	private final UserDetailsService userDetailsService;
	
	
	@Override
	protected void doFilterInternal(
			@NotNull HttpServletRequest request,
			@NotNull HttpServletResponse response, 
			@NotNull FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		final String username;
		
		
		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		jwt = authHeader.substring(7);
		username = jwtservice.extractUsername(jwt);
		if(username != null && SecurityContextHolder.getContext().getAuthentication()  == null) {
			UserDetails userdetails = this.userDetailsService.loadUserByUsername(username);
			if(jwtservice.isTokenValid(jwt, userdetails)) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userdetails, null, userdetails.getAuthorities());
				
				authToken.setDetails(
						new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		filterChain.doFilter(request, response);
		
		
		
	}

}
