package com.shoeplace.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import com.shoeplace.entity.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long> {
	Slice<Brand> findBrandInfoByKorNameContainingOrEngNameContaining(String korName, String engName, Pageable pageable);
}
