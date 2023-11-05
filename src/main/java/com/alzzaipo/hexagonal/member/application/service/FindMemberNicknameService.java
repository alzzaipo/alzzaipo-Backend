package com.alzzaipo.hexagonal.member.application.service;

import com.alzzaipo.hexagonal.common.Uid;
import com.alzzaipo.hexagonal.member.application.port.in.FindMemberNicknameQuery;
import com.alzzaipo.hexagonal.member.application.port.out.FindMemberPort;
import com.alzzaipo.hexagonal.member.domain.Member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindMemberNicknameService implements FindMemberNicknameQuery {

    private final FindMemberPort findMemberPort;

    @Override
    public String findMemberNickname(Uid memberUID) {
        Member member = findMemberPort.findMember(memberUID)
                .orElseThrow(() -> new RuntimeException("회원 조회 실패"));

        return member.getNickname();
    }
}
