package com.fullstack.recepcionArriendo.repository;

import com.fullstack.recepcionArriendo.model.RecepcionArriendo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecepcionArriendoRepository extends JpaRepository<RecepcionArriendo, Integer> {
}
