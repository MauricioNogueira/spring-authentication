package br.com.jwt.authentication.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private")
public class AuthenticatedController {

	@PostMapping
	public String message() {
		return "Usuário Logado :D";
	}
}
