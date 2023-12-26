package com.alzzaipo.member.application.port.out.member;

import com.alzzaipo.common.LoginType;
import com.alzzaipo.member.application.port.in.dto.MemberProfile;

public interface FindMemberProfilePort {

	MemberProfile findProfile(long memberId, LoginType currentLoginType);

}
