package com.alzzaipo.hexagonal.member.application.port.in;

import com.alzzaipo.hexagonal.common.LoginType;
import com.alzzaipo.hexagonal.common.Uid;
import com.alzzaipo.hexagonal.member.application.port.in.dto.MemberProfile;

public interface FindMemberProfileQuery {

    MemberProfile findMemberProfile(Uid memberUID, LoginType currentLoginType);
}
