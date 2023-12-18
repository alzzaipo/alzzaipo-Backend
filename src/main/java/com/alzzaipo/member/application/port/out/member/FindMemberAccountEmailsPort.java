package com.alzzaipo.member.application.port.out.member;

import com.alzzaipo.common.Uid;
import java.util.Set;

public interface FindMemberAccountEmailsPort {

	Set<String> findEmails(Uid memberId);
}
