package com.curso.ecommerce.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.repository.OrdenRepository;

@Service
public class OrdenServiceImpl implements IOrdenService{
	
	@Autowired
	private OrdenRepository ordenRepository;

	@Override
	public Orden save(Orden orden) {
		return ordenRepository.save(orden);
	}

	@Override
	public List<Orden> findAll() {
		return ordenRepository.findAll();
	}
	
	public String generarNumeroOrden() {
		int numero=0;
		String numero_concatenado="";
		List<Orden> ordenes=this.findAll();
		List<Integer> numeros=new ArrayList<>();
		ordenes.stream().forEach(o->numeros.add(Integer.parseInt(o.getNumero())));
		
		if(ordenes.isEmpty()) {
			numero=1;
		}else {
			numero=numeros.stream().max(Integer::compare).get();
			numero++;
		}
		
		int numeral_count=String.valueOf(numero).length();
		int zeros_count=10-numeral_count;
		
		String path="0".repeat(zeros_count)+"%d";
		numero_concatenado=String.format(path, numero);
		
		return numero_concatenado;
	}

	@Override
	public List<Orden> findByUsuario(Usuario usuario) {
		return ordenRepository.findByUsuario(usuario);
	}

	@Override
	public Optional<Orden> findById(Integer id) {
		return ordenRepository.findById(id);
	}

}
