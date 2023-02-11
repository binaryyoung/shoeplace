package com.shoeplace.service.address;

import static com.shoeplace.exception.ErrorCode.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shoeplace.dto.AddressCreateDto;
import com.shoeplace.dto.AddressInquireDto;
import com.shoeplace.dto.AddressUpdateDto;
import com.shoeplace.entity.Address;
import com.shoeplace.entity.User;
import com.shoeplace.exception.BusinessException;
import com.shoeplace.repository.AddressRepository;
import com.shoeplace.service.user.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AddressService {

	private final AddressRepository addressRepository;
	private final UserService userService;

	@Transactional
	public void createService(AddressCreateDto.Request request, String loginId) {
		User user = userService.getUserById(loginId);
		List<Address> addresses = addressRepository.findByUser(user);

		if (addresses.size() >= 10) {
			throw new BusinessException(ADDRESS_LIMIT_EXCEEDED);
		}

		if (addresses.stream()
			.map(Address::getNickname)
			.anyMatch(a -> a.equals(request.getNickname()))) {
			throw new BusinessException(DUPLICATED_ADDRESS_NICKNAME);
		}

		addressRepository.save(Address.builder()
			.nickname(request.getNickname())
			.address1(request.getAddress1())
			.address2(request.getAddress2())
			.receiverName(request.getReceiverName())
			.phoneNumber(request.getPhoneNumber())
			.user(user)
			.build());
	}

	@Transactional(readOnly = true)
	public List<AddressInquireDto.Response> inquireAddress(String loginId) {
		User user = userService.getUserById(loginId);
		return addressRepository.findByUserOrderByAddressId(user).stream()
			.map(AddressInquireDto.Response::of)
			.collect(Collectors.toList());
	}

	@Transactional
	public void updateAddress(AddressUpdateDto.Request request, String loginId) {
		Address address = addressRepository.findById(request.getId()).orElseThrow(
			() -> new BusinessException(ADDRESS_NOT_FOUND)
		);
		address.changeInfo(request);
	}

	@Transactional
	public void deleteAddress(long id) {
		Address address = addressRepository.findById(id).orElseThrow(
			() -> new BusinessException(ADDRESS_NOT_FOUND)
		);
		addressRepository.delete(address);
	}
}
