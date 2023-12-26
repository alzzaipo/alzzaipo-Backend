package com.alzzaipo.member.application.port.out.account.social;

import com.alzzaipo.member.application.port.out.dto.AccessToken;
import com.alzzaipo.member.application.port.out.dto.UserProfile;

public interface FetchKakaoUserProfilePort {

    UserProfile fetchKakaoUserProfile(AccessToken accessToken);
}
