package com.alzzaipo.hexagonal.member.domain;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Member {

    private final UUID uuid;
    private final String nickname;

    public Member(UUID uuid, String nickname) {
        this.uuid = uuid;
        this.nickname = nickname;
    }
}
