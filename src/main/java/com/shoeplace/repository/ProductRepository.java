package com.shoeplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shoeplace.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
