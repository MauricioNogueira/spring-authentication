package br.com.jwt.authentication.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.jwt.authentication.dto.TokenResponseDto;
import br.com.jwt.authentication.models.Usuario;
import br.com.jwt.authentication.requests.AuthRequest;
import br.com.jwt.authentication.security.JWTTokenUtil;

@RestController
public class AuthController {
	
	@Autowired
	private JWTTokenUtil jwtTokenUtil;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping("/auth")
	public ResponseEntity<TokenResponseDto> auth(@RequestBody @Valid AuthRequest request) {
		try {
			System.out.println("Inicio");
			Authentication auth = this.authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
			);
			
			Usuario usuario = (Usuario) auth.getPrincipal();
			
			String token = this.jwtTokenUtil.generateAccessToken(usuario);
			
			return ResponseEntity.ok().body(new TokenResponseDto(token, "Bearer"));
		} catch (BadCredentialsException ex) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }
	}
}
