package com.shoeplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shoeplace.entity.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
}
