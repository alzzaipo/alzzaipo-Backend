package com.alzzaipo.member.application.port.in.member;

import com.alzzaipo.common.Id;
import java.util.Set;

public interface FindMemberAccountEmailsQuery {

	Set<String> findEmails(Id memberId);
}
