package com.fullstack.devolucion.repository;

import com.fullstack.devolucion.model.Devolucion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DevolucionRepository extends JpaRepository<Devolucion, Integer> {
}
