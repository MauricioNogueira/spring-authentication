package br.com.jwt.authentication.security;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.jwt.authentication.repository.UsuarioRepository;

@Component
public class JWTTokenFilter extends OncePerRequestFilter {
	
	private UsuarioRepository usuarioRepository;
	private JWTTokenUtil jwtTokenUtil;
	
	public JWTTokenFilter(UsuarioRepository usuarioRepository, JWTTokenUtil jwtTokenUtil) {
		this.usuarioRepository = usuarioRepository;
		this.jwtTokenUtil = jwtTokenUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String headerAuthentication = request.getHeader("Authorization");
		
		if (headerAuthentication == null || !headerAuthentication.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		} else {			
			String token = headerAuthentication.substring(7);
			
			if (!jwtTokenUtil.validate(token)) {
				filterChain.doFilter(request, response);
				
				return;
			}
			
			String username = this.jwtTokenUtil.getUsername(token);
			
			System.out.println("Nome: "+ username);
			
			Optional<UserDetails> optional = this.usuarioRepository.findByUsername(username);
			
			if (optional.isPresent()) {
				UserDetails userDetails = optional.get();
				
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), null);
				
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(authentication);
				
				filterChain.doFilter(request, response);
			}
		}
		
	}	
}
