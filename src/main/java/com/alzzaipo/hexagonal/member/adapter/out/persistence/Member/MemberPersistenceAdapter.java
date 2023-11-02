package com.alzzaipo.hexagonal.member.adapter.out.persistence.Member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberPersistenceAdapter {

    private final NewMemberRepository memberRepository;
}
