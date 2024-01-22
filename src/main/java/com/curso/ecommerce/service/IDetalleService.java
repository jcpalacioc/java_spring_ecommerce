package com.curso.ecommerce.service;

import java.util.List;

import com.curso.ecommerce.model.DetalleOrden;
import com.curso.ecommerce.model.Orden;

public interface IDetalleService {
	DetalleOrden save(DetalleOrden orden);
	List<DetalleOrden> findByOrden(Orden orden);
}
