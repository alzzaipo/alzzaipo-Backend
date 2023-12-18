package com.alzzaipo.member.application.port.in.member;

import com.alzzaipo.common.Uid;
import java.util.Set;

public interface FindMemberAccountEmailsQuery {

	Set<String> findEmails(Uid memberId);
}
