package com.curso.ecommerce.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.curso.ecommerce.model.DetalleOrden;
import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.IDetalleService;
import com.curso.ecommerce.service.IOrdenService;
import com.curso.ecommerce.service.IUsuarioService;
import com.curso.ecommerce.service.ProductoService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class HomeController {
	private final Logger logger=LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private ProductoService productoService;
	
	private List<DetalleOrden> detalles=new ArrayList<>();
	
	private Orden orden=new Orden();
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private IOrdenService ordenService;
	
	@Autowired
	private IDetalleService detalleService;
	
	@GetMapping("")
	public String home(Model model,HttpSession session) {
		model.addAttribute("productos",productoService.findAll());
		logger.info("session_id {}",session.getAttribute("id_usuario"));
		model.addAttribute("sesion",session.getAttribute("id_usuario"));
		return "usuario/home";
	}
	
	@GetMapping("productohome/{id}")
	public String productoHome(@PathVariable Integer id,Model model,HttpSession session) {
		
		logger.info("id enviado como parametro {}",id);
		Producto producto=new Producto();
		Optional<Producto> productoOpcional=productoService.get(id);
		producto=productoOpcional.get();
		model.addAttribute("producto",producto);
		model.addAttribute("sesion",session.getAttribute("id_usuario"));
		return "usuario/productohome";
	}
	@PostMapping("/cart")
	public String add_cart(
			@RequestParam Integer id,
			@RequestParam Integer cantidad,
			Model model,
			HttpSession session
			) {
		DetalleOrden detalle=new DetalleOrden();
		Producto producto=new Producto();
		double suma_total=0;
		
		Optional<Producto> optionalProducto=productoService.get(id);
		logger.info("Producto: {}",optionalProducto.get());
		logger.info("Cantidad: {}",cantidad);
		producto=optionalProducto.get();
		
		detalle.setCantidad(cantidad);
		detalle.setPrecio(producto.getPrecio());
		detalle.setNombre(producto.getNombre());
		detalle.setProducto(producto);
		detalle.setTotal(producto.getPrecio()*cantidad);
		
		Integer id_producto=producto.getId();
		boolean ingresado=detalles.stream().anyMatch(p-> p.getProducto().getId()==id_producto);
		
		if(!ingresado) {
			detalles.add(detalle);
		}
		
		suma_total=detalles.stream().mapToDouble(dt->dt.getTotal()).sum();
		orden.setTotal(suma_total);
		model.addAttribute("cart",detalles);
		model.addAttribute("orden",orden);
		logger.info("is admin {}",session.getAttribute(""));
		model.addAttribute("sesion",session.getAttribute("id_usuario"));
		
		return "usuario/carrito";
	}
	@GetMapping("/delete/cart/{id}")
	public String delete_product(@PathVariable Integer id,Model model,HttpSession session) {
		List<DetalleOrden> ordenesNuevas=new ArrayList<>();
		
		for(DetalleOrden detalle:detalles) {
			if(detalle.getProducto().getId()!=id) {
				ordenesNuevas.add(detalle);
			}
		}
		detalles=ordenesNuevas;
		
		double suma_total=0;
		suma_total=detalles.stream().mapToDouble(dt->dt.getTotal()).sum();
		orden.setTotal(suma_total);
		model.addAttribute("cart",detalles);
		model.addAttribute("orden",orden);
		model.addAttribute("sesion",session.getAttribute("id_usuario"));
		
		return "usuario/carrito";
	}
	
	@GetMapping("/getCart")
	public String getCart(Model model,HttpSession session) {
		model.addAttribute("cart",detalles);
		model.addAttribute("orden",orden);
		logger.info("is admin {}",session.getAttribute(""));
		model.addAttribute("sesion",session.getAttribute("id_usuario"));
		
		return "usuario/carrito";
	}
	
	@GetMapping("/order")
	public String order(Model model,HttpSession session) {
		if(session.getAttribute("id_usuario")==null) {
			return "redirect:/usuario/login";
		}
		
		Usuario usuario=usuarioService.findById(Integer.parseInt(session.getAttribute("id_usuario").toString())).get();
		model.addAttribute("cart",detalles);
		model.addAttribute("orden",orden);
		model.addAttribute("usuario",usuario);
		model.addAttribute("sesion",session.getAttribute("id_usuario"));
		
		return "usuario/resumenorden";
	}
	
	@GetMapping("/saveOrder")
	public String save_order(Model model,HttpSession session) {
		Date fechaCreacion=new Date();
		orden.setFechaCreacion(fechaCreacion);
		orden.setNumero(ordenService.generarNumeroOrden());
		
		Usuario usuario=usuarioService.findById(Integer.parseInt(session.getAttribute("id_usuario").toString())).get();
		orden.setUsuario(usuario);
		ordenService.save(orden);
		
		for(DetalleOrden dt:detalles) {
			dt.setOrden(orden);
			detalleService.save(dt);
		}
		
		orden=new Orden();
		detalles.clear();
		model.addAttribute("sesion",session.getAttribute("id_usuario"));
		return "redirect:/usuario/compras";
	}
	@PostMapping("/search")
	public String seacrhProduct(@RequestParam String nombre,Model model,HttpSession session) {
		logger.info("Nombre del producto: {}",nombre);
		List<Producto> productos=productoService.findAll()
				.stream()
				.filter(p->p.getNombre().toLowerCase().contains(nombre.toLowerCase()))
				.collect(Collectors.toList());
		
		model.addAttribute("productos",productos);
		
		return "usuario/home";
	}
}
