package com.alzzaipo.hexagonal.member.application.service;

import com.alzzaipo.hexagonal.common.Uid;
import com.alzzaipo.hexagonal.member.application.port.in.WithdrawMemberUseCase;
import com.alzzaipo.hexagonal.member.application.port.out.WithdrawMemberPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WithdrawMemberService implements WithdrawMemberUseCase {

    private final WithdrawMemberPort withdrawMemberPort;

    @Override
    public void withdrawMember(Uid memberUID) {
        try {
            withdrawMemberPort.withdrawMember(memberUID);
        } catch (Exception e) {
            throw new RuntimeException("회원 탈퇴 실패");
        }
    }
}
