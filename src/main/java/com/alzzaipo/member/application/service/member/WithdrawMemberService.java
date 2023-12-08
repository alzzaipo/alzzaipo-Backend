package com.alzzaipo.member.application.service.member;

import com.alzzaipo.common.Uid;
import com.alzzaipo.member.application.port.in.member.WithdrawMemberUseCase;
import com.alzzaipo.member.application.port.out.member.WithdrawMemberPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WithdrawMemberService implements WithdrawMemberUseCase {

	private final WithdrawMemberPort withdrawMemberPort;

	@Override
	public void withdrawMember(Uid memberUID) {
		withdrawMemberPort.withdrawMember(memberUID);
	}
}
