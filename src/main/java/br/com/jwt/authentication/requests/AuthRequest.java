package br.com.jwt.authentication.requests;

import javax.validation.constraints.NotBlank;

public class AuthRequest {

	@NotBlank
	private String username;
	
	@NotBlank
	private String password;

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String toString() {
		return "AuthRequest [username=" + username + ", password=" + password + "]";
	}
}
