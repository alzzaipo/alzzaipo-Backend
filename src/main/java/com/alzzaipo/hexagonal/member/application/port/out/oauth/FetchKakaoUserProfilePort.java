package com.alzzaipo.hexagonal.member.application.port.out.oauth;

import com.alzzaipo.hexagonal.member.application.port.out.dto.AccessToken;
import com.alzzaipo.hexagonal.member.application.port.out.dto.UserProfile;

public interface FetchKakaoUserProfilePort {

    UserProfile fetchKakaoUserProfile(AccessToken accessToken);
}
