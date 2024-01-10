package com.alzzaipo.member.application.port.in.account.social;

import com.alzzaipo.member.application.port.in.dto.LinkKakaoAccountCommand;

public interface LinkKakaoAccountUseCase {

    void linkKakaoAccount(LinkKakaoAccountCommand command);
}
