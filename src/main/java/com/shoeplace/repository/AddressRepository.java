package com.shoeplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shoeplace.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
