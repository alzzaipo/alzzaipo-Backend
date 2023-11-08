package com.alzzaipo.member.application.service.member;

import com.alzzaipo.common.Uid;
import com.alzzaipo.member.application.port.in.member.FindMemberNicknameQuery;
import com.alzzaipo.member.application.port.out.member.FindMemberPort;
import com.alzzaipo.member.domain.member.Member;
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
