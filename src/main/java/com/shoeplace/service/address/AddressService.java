package com.shoeplace.service.address;

import static com.shoeplace.exception.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shoeplace.dto.AddressCreateDto;
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
}
