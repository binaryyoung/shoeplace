package com.shoeplace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shoeplace.entity.Address;
import com.shoeplace.entity.User;

public interface AddressRepository extends JpaRepository<Address, Long> {
	List<Address> findByUser(User user);

	List<Address> findByUserOrderByAddressId(User user);

	boolean existsByUserAndNickname(String loginId, String nickname);
}
