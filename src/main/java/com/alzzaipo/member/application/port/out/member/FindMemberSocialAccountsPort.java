package com.alzzaipo.member.application.port.out.member;

import com.alzzaipo.common.Uid;
import com.alzzaipo.member.domain.account.social.SocialAccount;

import java.util.List;

public interface FindMemberSocialAccountsPort {

    List<SocialAccount> findMemberSocialAccounts(Uid memberUID);
}
