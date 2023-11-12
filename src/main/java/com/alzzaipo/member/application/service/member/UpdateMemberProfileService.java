package com.alzzaipo.member.application.service.member;

import com.alzzaipo.common.Email;
import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.email.application.port.out.CheckEmailVerifiedPort;
import com.alzzaipo.member.application.port.in.account.local.CheckLocalAccountEmailAvailabilityQuery;
import com.alzzaipo.member.application.port.in.dto.UpdateMemberProfileCommand;
import com.alzzaipo.member.application.port.in.member.UpdateMemberProfileUseCase;
import com.alzzaipo.member.application.port.out.account.local.ChangeLocalAccountEmail;
import com.alzzaipo.member.application.port.out.account.local.FindLocalAccountByMemberUidPort;
import com.alzzaipo.member.application.port.out.dto.SecureLocalAccount;
import com.alzzaipo.member.application.port.out.member.ChangeMemberNicknamePort;
import com.alzzaipo.member.application.port.out.member.FindMemberPort;
import com.alzzaipo.member.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UpdateMemberProfileService implements UpdateMemberProfileUseCase {

    private final FindMemberPort findMemberPort;
    private final FindLocalAccountByMemberUidPort findLocalAccountByMemberUidPort;
    private final ChangeMemberNicknamePort changeMemberNicknamePort;
    private final ChangeLocalAccountEmail changeLocalAccountEmail;
    private final CheckLocalAccountEmailAvailabilityQuery checkLocalAccountEmailAvailabilityQuery;
    private final CheckEmailVerifiedPort checkEmailVerifiedPort;

    @Override
    @Transactional
    public void updateMemberProfile(UpdateMemberProfileCommand command) {
        Member member = findMemberPort.findMember(command.getMemberUID())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "회원 조회 실패"));

        changeMemberNickname(command);

        findLocalAccountByMemberUidPort.findLocalAccountByMemberUid(member.getUid())
                .ifPresent(localAccount -> changeLocalMemberEmail(command, localAccount));
    }

    private void changeMemberNickname(UpdateMemberProfileCommand command) {
        changeMemberNicknamePort.changeMemberNickname(
                command.getMemberUID(),
                command.getNickname());
    }

    private void changeLocalMemberEmail(UpdateMemberProfileCommand command, SecureLocalAccount localAccount) {
        Email currentEmail = localAccount.getEmail();
        Email newEmail = command.getEmail();

        if (currentEmail.equals(newEmail)) {
            return; // 이메일이 변경되지 않은 경우 처리하지 않음
        }

        if (!checkLocalAccountEmailAvailabilityQuery.checkLocalAccountEmailAvailability(newEmail)) {
            throw new CustomException(HttpStatus.CONFLICT, "오류: 이미 등록된 이메일");
        }

        if (!checkEmailVerifiedPort.checkEmailVerified(newEmail)) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "오류: 인증되지 않은 이메일");
        }

        changeLocalAccountEmail.changeLocalAccountEmail(
                localAccount.getAccountId(),
                newEmail);
    }
}
