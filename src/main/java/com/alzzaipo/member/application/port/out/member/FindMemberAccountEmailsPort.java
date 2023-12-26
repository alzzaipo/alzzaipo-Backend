package com.alzzaipo.member.application.port.out.member;

import com.alzzaipo.common.Id;
import java.util.Set;

public interface FindMemberAccountEmailsPort {

	Set<String> findEmails(Id memberId);
}
