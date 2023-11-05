package com.alzzaipo.hexagonal.member.adapter.out.persistence.Member;

import com.alzzaipo.hexagonal.member.adapter.out.persistence.LocalAccount.LocalAccountJpaEntity;
import com.alzzaipo.hexagonal.member.adapter.out.persistence.SocialAccount.SocialAccountJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class MemberJpaEntity {

    @Id
    private Long uid;

    @Column(nullable = false)
    private String nickname;

    @OneToOne(mappedBy = "memberJpaEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private LocalAccountJpaEntity localAccountJpaEntity;

    @OneToMany(mappedBy = "memberJpaEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<SocialAccountJpaEntity> socialAccountJpaEntities;

    public MemberJpaEntity(Long uid, String nickname) {
        this.uid = uid;
        this.nickname = nickname;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }
}
