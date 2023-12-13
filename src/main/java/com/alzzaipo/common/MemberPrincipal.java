package com.alzzaipo.common;

import com.alzzaipo.member.domain.member.Role;
import java.io.Serializable;
import lombok.Getter;

@Getter
public class MemberPrincipal implements Serializable {

	private final Uid memberUID;
	private final LoginType currentLoginType;
	private final Role role;

	public MemberPrincipal(Uid memberUID, LoginType currentLoginType, Role role) {
		this.memberUID = memberUID;
		this.currentLoginType = currentLoginType;
		this.role = role;
	}
}
