package br.com.jwt.authentication.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.jwt.authentication.repository.UsuarioRepository;

@Service
public class UserDetailsImpl implements UserDetailsService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserDetails> optional = this.usuarioRepository.findByUsername(username);
		
		if (optional.isPresent()) {
			return optional.get();
		}
		
		return null;
	}
}
