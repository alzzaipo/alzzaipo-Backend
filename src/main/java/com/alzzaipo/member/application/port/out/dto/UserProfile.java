package com.alzzaipo.member.application.port.out.dto;

import com.alzzaipo.common.Email;
import lombok.Getter;

@Getter
public class UserProfile {

    private final String nickname;
    private final Email email;

    public UserProfile(String nickname, String email) {
        this.nickname = nickname;
        this.email = new Email(email);
    }
}
