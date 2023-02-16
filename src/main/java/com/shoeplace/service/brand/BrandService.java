package com.shoeplace.service.brand;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shoeplace.dto.BrandInfoDto;
import com.shoeplace.repository.BrandRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class BrandService {

	private final BrandRepository brandRepository;

	public Slice<BrandInfoDto.Response> inquireBrandInfo(String name, int page) {
		PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC, "engName"));
		return brandRepository.findBrandInfoByKorNameContainingOrEngNameContaining(name, name, pageRequest)
			.map(BrandInfoDto.Response::of);
	}
}
