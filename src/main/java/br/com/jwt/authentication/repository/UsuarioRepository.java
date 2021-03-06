package br.com.jwt.authentication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.jwt.authentication.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	Optional<UserDetails> findByUsername(String username);

}
