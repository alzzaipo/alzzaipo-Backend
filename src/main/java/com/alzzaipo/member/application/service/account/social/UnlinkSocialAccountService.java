package com.alzzaipo.member.application.service.account.social;

import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.member.application.port.in.account.social.UnlinkSocialAccountUseCase;
import com.alzzaipo.member.application.port.in.dto.UnlinkSocialAccountCommand;
import com.alzzaipo.member.application.port.out.account.local.FindLocalAccountByMemberUidPort;
import com.alzzaipo.member.application.port.out.account.social.DeleteSocialAccountPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UnlinkSocialAccountService implements UnlinkSocialAccountUseCase {

    private final FindLocalAccountByMemberUidPort findLocalAccountByMemberUidPort;
    private final DeleteSocialAccountPort deleteSocialAccountUsePort;

    @Override
    public void unlinkSocialAccountUseCase(UnlinkSocialAccountCommand command) {
        findLocalAccountByMemberUidPort.findByMemberId(command.getMemberUID())
                .orElseThrow(() -> new CustomException(HttpStatus.FORBIDDEN, "소셜 계정 연동 해지는 로컬 계정에서만 가능합니다."));

        deleteSocialAccountUsePort.deleteSocialAccount(
                command.getMemberUID(),
                command.getLoginType());
    }
}
