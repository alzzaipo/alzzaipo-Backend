package com.alzzaipo.member.domain.member;

import com.alzzaipo.common.Uid;
import com.alzzaipo.common.util.UidGenerator;
import lombok.Getter;

@Getter
public class Member {

	private final Uid uid;
	private final String nickname;
	private final Role role;

	public static Member create(String nickname) {
		return new Member(UidGenerator.generate(), nickname, Role.USER);
	}

	public Member(Uid uid, String nickname, Role role) {
		this.uid = uid;
		this.nickname = nickname;
		this.role = role;
	}
}
