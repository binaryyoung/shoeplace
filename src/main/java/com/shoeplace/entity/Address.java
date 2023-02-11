package com.shoeplace.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.shoeplace.dto.AddressUpdateDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Address extends BaseEntity {

	@Id
	@GeneratedValue
	private Long addressId;

	@Column(unique = true)
	private String nickname;
	private String address1;
	private String address2;
	private String receiverName;
	private String phoneNumber;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	public void changeInfo(AddressUpdateDto.Request request) {
		this.address1 = request.getAddress1();
		this.address2 = request.getAddress2();
		this.receiverName = request.getReceiverName();
		this.phoneNumber = request.getPhoneNumber();
	}
}