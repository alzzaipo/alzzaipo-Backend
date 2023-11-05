package com.alzzaipo.hexagonal.member.application.port.out.member;

import com.alzzaipo.hexagonal.common.Uid;
import com.alzzaipo.hexagonal.member.domain.account.social.SocialAccount;

import java.util.List;

public interface FindMemberSocialAccountsPort {

    List<SocialAccount> findMemberSocialAccounts(Uid memberUID);
}
