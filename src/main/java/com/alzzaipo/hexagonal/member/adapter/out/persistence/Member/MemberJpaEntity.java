package com.alzzaipo.hexagonal.member.adapter.out.persistence.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class MemberJpaEntity {

    @Id
    private Long uid;

    @Column(nullable = false)
    private String nickname;

    public MemberJpaEntity(Long uid, String nickname) {
        this.uid = uid;
        this.nickname = nickname;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }
}
