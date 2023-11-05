package com.alzzaipo.hexagonal.member.domain.member;

import com.alzzaipo.hexagonal.common.Uid;
import com.alzzaipo.hexagonal.common.util.UidGenerator;
import lombok.Getter;

@Getter
public class Member {

    private final Uid uid;
    private final String nickname;

    public static Member create(String nickname) {
        return new Member(UidGenerator.generate(), nickname);
    }

    public Member(Uid uid, String nickname) {
        this.uid = uid;
        this.nickname = nickname;
    }
}
