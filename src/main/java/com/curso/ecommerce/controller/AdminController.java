package com.curso.ecommerce.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.curso.ecommerce.model.DetalleOrden;
import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.repository.DetalleRepository;
import com.curso.ecommerce.service.IDetalleService;
import com.curso.ecommerce.service.IOrdenService;
import com.curso.ecommerce.service.IUsuarioService;
import com.curso.ecommerce.service.ProductoService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private IOrdenService ordenService;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private IDetalleService detalleService;
	
	@GetMapping("")
	public String home(Model model, HttpSession session) {
		List<Producto> productos=productoService.findAll();
		model.addAttribute("productos",productos);
		return "admin/home";
	}
	@GetMapping("/usuarios")
	public String usuarios(Model model) {
		model.addAttribute("usuarios",usuarioService.findAll());
		return "admin/usuarios";
	}
	@GetMapping("/ordenes")
	public String ordenes(Model model) {
		model.addAttribute("ordenes",ordenService.findAll());
		return "admin/ordenes";
	}
	@GetMapping("/detalle/{id}")
	public String detalle(@PathVariable Integer id,Model model) {
		Orden orden=ordenService.findById(id).get();
		model.addAttribute("detalles",orden.getDetalles());
		return "admin/detalleorden";
	}
}
