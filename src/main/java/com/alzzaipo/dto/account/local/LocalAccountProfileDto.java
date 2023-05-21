package com.alzzaipo.dto.account.local;

import com.alzzaipo.domain.account.social.SocialCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class LocalAccountProfileDto {
    private String accountId;
    private String nickname;
    private List<SocialCode> linkedSocialLoginTypes;
}
