package com.shoeplace.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Users extends BaseEntity{

	@Id
	@GeneratedValue
	private Long userId;

	private String nickName;
	private String password;
	private String phoneNumber;
	private boolean emailAuthYn;

	@Enumerated(value = EnumType.STRING)
	private UserRole role;

	@Enumerated(value = EnumType.STRING)
	private UserStatus status;
}
