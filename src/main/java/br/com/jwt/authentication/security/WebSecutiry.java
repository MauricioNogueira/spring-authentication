package br.com.jwt.authentication.security;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.jwt.authentication.repository.UsuarioRepository;

@Configuration
@EnableWebSecurity
public class WebSecutiry extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private JWTTokenUtil jwtTokenUtil;
	
	/**
	 * AuthenticateManager precisamos implementar pois ele não é acessível publicamente por padrão pelo spring
	 * Assim que implementamos, podemos usar em qualquer lugar
	 *  */
	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		// TODO Auto-generated method stub
		return super.authenticationManager();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		/**
		 *  Habilita o cors e desabilita o CSRF(cross-site request forgery)
		 * */
		http = http.cors().and().csrf().disable();
		
		/**
		 * Configuramos que nossa requisição não irá guardas dados em sessão
		 * Com isso, todas as requisições serão necessárias mandar um token de autenticacão
		 * */
		http = http.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();
		
		/**
		 * Caso a requisição não encontre o usuário solicitando acesso, será lancado uma exceção
		 *  */
		http = http.exceptionHandling()
				.authenticationEntryPoint((request, response, ex) -> {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
				}).and();
		
		/**
		 * Aqui estamos configurando as rotas que necessitaram de token
		 * Somente requisição POST para "/auth" está livre
		 * */
		http.authorizeRequests()
				.antMatchers(HttpMethod.POST, "/auth").permitAll()
				.anyRequest().authenticated();
		
		http.addFilterBefore(new JWTTokenFilter(this.usuarioRepository, this.jwtTokenUtil), UsernamePasswordAuthenticationFilter.class);
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}