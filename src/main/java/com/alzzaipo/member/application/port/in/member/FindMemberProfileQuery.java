package com.alzzaipo.member.application.port.in.member;

import com.alzzaipo.common.LoginType;
import com.alzzaipo.common.Uid;
import com.alzzaipo.member.application.port.in.dto.MemberProfile;

public interface FindMemberProfileQuery {

    MemberProfile findMemberProfile(Uid memberUID, LoginType currentLoginType);
}
