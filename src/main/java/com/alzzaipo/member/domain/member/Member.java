package com.alzzaipo.member.domain.member;

import com.alzzaipo.common.Id;
import lombok.Getter;

@Getter
public class Member {

	private final Id id;
	private final String nickname;
	private final Role role;

	public Member(Id id, String nickname, Role role) {
		this.id = id;
		this.nickname = nickname;
		this.role = role;
	}

	public static Member build(String nickname) {
		return new Member(Id.generate(), nickname, Role.USER);
	}
}
