package com.alzzaipo.member.application.port.in.account.social;

import com.alzzaipo.member.application.port.in.dto.UnlinkSocialAccountCommand;

public interface UnlinkSocialAccountUseCase {

    void unlinkSocialAccountUseCase(UnlinkSocialAccountCommand command);
}
