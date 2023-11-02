package com.alzzaipo.hexagonal.member.domain;

import com.alzzaipo.hexagonal.common.UidGenerator;
import lombok.Getter;

@Getter
public class Member {

    private final long uid;
    private final String nickname;

    public static Member create(String nickname) {
        return new Member(UidGenerator.generate(), nickname);
    }

    public Member(long uid, String nickname) {
        this.uid = uid;
        this.nickname = nickname;
    }
}
