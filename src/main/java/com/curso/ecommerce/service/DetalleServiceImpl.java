package com.curso.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curso.ecommerce.model.DetalleOrden;
import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.repository.DetalleRepository;

@Service
public class DetalleServiceImpl implements IDetalleService{
	
	@Autowired
	private DetalleRepository detalleRepository;
	
	@Override
	public DetalleOrden save(DetalleOrden orden) {
		return detalleRepository.save(orden);
	}

	@Override
	public List<DetalleOrden> findByOrden(Orden orden) {
		return detalleRepository.findByOrden(orden);
	}


	
}
