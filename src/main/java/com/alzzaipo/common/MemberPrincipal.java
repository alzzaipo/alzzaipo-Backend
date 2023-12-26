package com.alzzaipo.common;

import com.alzzaipo.member.domain.member.Role;
import java.io.Serializable;
import lombok.Getter;

@Getter
public class MemberPrincipal implements Serializable {

	private final Id memberId;
	private final LoginType currentLoginType;
	private final Role role;

	public MemberPrincipal(Id memberId, LoginType currentLoginType, Role role) {
		this.memberId = memberId;
		this.currentLoginType = currentLoginType;
		this.role = role;
	}
}
