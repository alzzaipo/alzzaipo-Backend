package com.alzzaipo.member.application.port.in.member;

import com.alzzaipo.common.LoginType;
import com.alzzaipo.common.Id;
import com.alzzaipo.member.application.port.in.dto.MemberProfile;

public interface FindMemberProfileQuery {

    MemberProfile findMemberProfile(Id memberId, LoginType currentLoginType);
}
