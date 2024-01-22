package com.curso.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.curso.ecommerce.model.DetalleOrden;
import com.curso.ecommerce.model.Orden;
import java.util.List;


@Repository
public interface DetalleRepository extends JpaRepository<DetalleOrden, Integer>{
	List<DetalleOrden> findByOrden(Orden orden);
}
