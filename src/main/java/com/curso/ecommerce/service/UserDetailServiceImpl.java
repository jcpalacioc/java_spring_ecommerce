package com.curso.ecommerce.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.curso.ecommerce.model.Usuario;

import jakarta.servlet.http.HttpSession;

@Service
public class UserDetailServiceImpl implements UserDetailsService{
	
	@Autowired
	private IUsuarioService usuarioService;
	
	
	@Autowired
	HttpSession session;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Usuario> optionalUser=usuarioService.findByEmail(username);
		if(optionalUser.isPresent()) {
			session.setAttribute("id_usuario", optionalUser.get().getId());
			Usuario usuario=optionalUser.get();
			return User.builder()
					.username(usuario.getNombre())
					.password(usuario.getPassword())
					.roles(usuario.getTipo())
					.build();
		}else {
			throw new UsernameNotFoundException("Usuario no encontrado");
		}
	}
	
}
