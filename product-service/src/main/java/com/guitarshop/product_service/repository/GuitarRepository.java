package com.guitarshop.product_service.repository;

import com.guitarshop.product_service.model.Guitar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuitarRepository extends JpaRepository<Guitar, Long> {
}
