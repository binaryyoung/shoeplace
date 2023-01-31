package com.shoeplace.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.shoeplace.exception.UserBusinessException;
import com.shoeplace.exception.UserErrorCode;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
public class User extends BaseEntity {

	@Id
	@GeneratedValue
	private Long userId;

	private String loginId;
	private String nickname;
	private String password;
	private String phoneNumber;
	private boolean emailAuthYn;

	@Enumerated(value = EnumType.STRING)
	private UserRole role;

	@Enumerated(value = EnumType.STRING)
	private UserStatus status;

	public void approveEmailAuth() {
		if (emailAuthYn) {
			throw new UserBusinessException(UserErrorCode.ALREADY_AUTHENTICATED_EMAIL_ACCOUNT);
		}
		status = UserStatus.AUTHENTICATED;
	}
}
