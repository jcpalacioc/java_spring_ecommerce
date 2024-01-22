package com.curso.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.curso.ecommerce.model.DetalleOrden;
import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Usuario;

import java.util.List;


@Repository
public interface OrdenRepository extends JpaRepository<Orden, Integer>{
	List<Orden> findByUsuario(Usuario usuario);
}
