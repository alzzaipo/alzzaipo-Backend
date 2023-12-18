package com.alzzaipo.member.application.service.member;

import com.alzzaipo.common.Uid;
import com.alzzaipo.member.application.port.in.member.FindMemberAccountEmailsQuery;
import com.alzzaipo.member.application.port.out.member.FindMemberAccountEmailsPort;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindMemberAccountEmailsService implements FindMemberAccountEmailsQuery {

	private final FindMemberAccountEmailsPort findMemberAccountEmailsPort;

	@Override
	public Set<String> findEmails(Uid memberId) {
		return findMemberAccountEmailsPort.findEmails(memberId);
	}
}
