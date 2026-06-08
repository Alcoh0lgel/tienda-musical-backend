package com.fullstack.Arriendo.repository;

import com.fullstack.Arriendo.model.Arriendo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArriendoRepository extends JpaRepository<Arriendo, Integer> {
}
