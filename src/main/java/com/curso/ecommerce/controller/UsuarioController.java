package com.curso.ecommerce.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.IOrdenService;
import com.curso.ecommerce.service.IUsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
	private final Logger logger=LoggerFactory.getLogger(UsuarioController.class);
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private IOrdenService ordenService;
	
	BCryptPasswordEncoder passEncoder=new BCryptPasswordEncoder();
	
	@GetMapping("/registro")
	public String create() {
		return "usuario/registro";
	}
	
	@PostMapping("/save")
	public String save(Usuario usuario) {
		logger.info("usuario registro: {}",usuario);
		usuario.setTipo("USER");
		usuario.setPassword(passEncoder.encode(usuario.getPassword()));
		usuarioService.save(usuario);
		return "redirect:/";
	}
	
	
	@GetMapping("/login")
	public String login() {
		return "usuario/login";
	}
	
	@GetMapping("/error")
	public String error(Model model) {
		model.addAttribute("alertas","Usuario o contraseña invalidos");
		return "usuario/login";
	}
	
	
	@GetMapping("/acceder")
	public String acceder(Usuario usuario,HttpSession session) {
		logger.info("accesos: {}",usuario);
		Optional<Usuario> user=usuarioService.findById((Integer) session.getAttribute("id_usuario"));
		if(user.isPresent()) {
			session.setAttribute("id_usuario", user.get().getId());
			if(user.get().getTipo().equals("ADMIN")) {
				return "redirect:/admin";
			}
		}else {
			logger.info("usuario no encontrado");
		}
		return "redirect:/";
	}
	@GetMapping("/compras")
	public String obtener_compras(Model model,HttpSession session) {
		if(session.getAttribute("id_usuario")==null) {
			return "redirect:/usuario/login";
		}
		Usuario usuario=usuarioService.findById(Integer.parseInt(session.getAttribute("id_usuario").toString())).get();
		model.addAttribute("sesion",session.getAttribute("id_usuario"));
		List<Orden> ordenes=ordenService.findByUsuario(usuario);
		model.addAttribute("ordenes",ordenes);
		
		return "usuario/compras";
	}
	@GetMapping("/detalles/{id}")
	public String detalle_compras(@PathVariable Integer id,HttpSession session,Model model) {
		if(session.getAttribute("id_usuario")==null) {
			return "redirect:/usuario/login";
		}
		Orden orden=ordenService.findById(id).get();
		model.addAttribute("sesion",session.getAttribute("id_usuario"));
		model.addAttribute("detalles",orden.getDetalles());
		
		return "usuario/detallecompra";
	}
	@GetMapping("/cerrar")
	public String cerrar(HttpSession session) {
		session.removeAttribute("id_usuario");
		session.removeAttribute("");
		return "redirect:/";
	}
	
}
