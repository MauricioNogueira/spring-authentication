package br.com.jwt.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.jwt.authentication.models.Usuario;
import br.com.jwt.authentication.repository.UsuarioRepository;

@SpringBootApplication
public class AuthenticationApplication implements CommandLineRunner {
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Usuario usuario = new Usuario("mauricio", new BCryptPasswordEncoder().encode("12345").toString());
		
		this.usuarioRepository.save(usuario);
	}

}
