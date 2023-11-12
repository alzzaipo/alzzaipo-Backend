package com.alzzaipo.member.application.service.member;

import com.alzzaipo.common.Uid;
import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.member.application.port.in.member.WithdrawMemberUseCase;
import com.alzzaipo.member.application.port.out.member.FindMemberPort;
import com.alzzaipo.member.application.port.out.member.WithdrawMemberPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WithdrawMemberService implements WithdrawMemberUseCase {

    private final FindMemberPort findMemberPort;
    private final WithdrawMemberPort withdrawMemberPort;

    @Override
    public void withdrawMember(Uid memberUID) {
        findMemberPort.findMember(memberUID)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "회원 조회 실패"));

        withdrawMemberPort.withdrawMember(memberUID);
    }
}
