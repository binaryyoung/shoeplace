package com.shoeplace.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.shoeplace.dto.ProductCreateDto;
import com.shoeplace.entity.Brand;
import com.shoeplace.entity.Product;
import com.shoeplace.entity.ProductImage;
import com.shoeplace.exception.BusinessException;
import com.shoeplace.exception.ErrorCode;
import com.shoeplace.repository.BrandRepository;
import com.shoeplace.repository.ProductImageRepository;
import com.shoeplace.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private static final String FILE_URL_FORMAT = "https://%s/img%s";

	private final BrandRepository brandRepository;
	private final ProductRepository productRepository;
	private final ProductImageRepository productImageRepository;

	private final AmazonS3Client amazonS3Client;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Transactional
	public ProductCreateDto.Response createProduct(ProductCreateDto.Request request,
		MultipartFile[] files) {

		Brand brand = brandRepository.findById(request.getBrandId()).orElseThrow(
			() -> new BusinessException(ErrorCode.BRAND_NOT_FOUND)
		);

		Product product = Product.builder()
			.korName(request.getKorName())
			.engName(request.getEngName())
			.modelNumber(request.getModelNumber())
			.minSize(request.getMinSize())
			.maxSize(request.getMaxSize())
			.unitSize(request.getUnitSize())
			.launchPrice(request.getLaunchPrice())
			.launchDate(request.getLaunchDate())
			.brand(brand)
			.build();

		productRepository.save(product);

		List<ProductImage> productImageList = new ArrayList<>();
		for (int i = 0; i < files.length; i++) {
			String fileName = UUID.randomUUID().toString();
			String fileUrl = createFileUrl(bucket, fileName);

			ProductImage productImage = ProductImage.builder()
				.originName(files[i].getOriginalFilename())
				.resizingName(fileName)
				.resizingPath(fileUrl)
				.ext(files[i].getContentType())
				.orders((long)i)
				.build();

			productImageList.add(productImage);

			try {
				ObjectMetadata metadata = new ObjectMetadata();
				metadata.setContentType(files[i].getContentType());
				metadata.setContentLength(files[i].getSize());
				amazonS3Client.putObject(bucket, fileName, files[i].getInputStream(), metadata);
			} catch (IOException e) {
				throw new BusinessException(ErrorCode.FILE_UPLOAD_FAIL);
			}
		}
		productImageRepository.saveAll(productImageList);

		return ProductCreateDto.Response.of(product);
	}

	private String createFileUrl(String bucket, String fileName) {
		return String.format(FILE_URL_FORMAT, bucket, fileName);
	}
}
