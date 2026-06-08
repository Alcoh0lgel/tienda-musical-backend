package com.fullstack.productoArriendo.repository;

import com.fullstack.productoArriendo.model.ProductoArriendo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoArriendoRepository extends JpaRepository<ProductoArriendo, Integer> {
}
