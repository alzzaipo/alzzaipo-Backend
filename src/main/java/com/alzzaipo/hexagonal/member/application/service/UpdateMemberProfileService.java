package com.alzzaipo.hexagonal.member.application.service;

import com.alzzaipo.hexagonal.common.Email;
import com.alzzaipo.hexagonal.email.application.port.out.CheckEmailVerifiedPort;
import com.alzzaipo.hexagonal.member.application.port.in.CheckLocalAccountEmailAvailabilityQuery;
import com.alzzaipo.hexagonal.member.application.port.in.UpdateMemberProfileUseCase;
import com.alzzaipo.hexagonal.member.application.port.in.dto.UpdateMemberProfileCommand;
import com.alzzaipo.hexagonal.member.application.port.out.ChangeLocalAccountEmail;
import com.alzzaipo.hexagonal.member.application.port.out.ChangeMemberNicknamePort;
import com.alzzaipo.hexagonal.member.application.port.out.FindLocalAccountByMemberUidPort;
import com.alzzaipo.hexagonal.member.application.port.out.FindMemberPort;
import com.alzzaipo.hexagonal.member.application.port.out.dto.SecureLocalAccount;
import com.alzzaipo.hexagonal.member.domain.Member.Member;
import lombok.RequiredArgsConstructor;
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
                .orElseThrow(() -> new RuntimeException("회원 조회 실패"));

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
            throw new RuntimeException("프로필 정보 변경 실패 : 이미 등록된 이메일");
        }

        if (!checkEmailVerifiedPort.checkEmailVerified(newEmail)) {
            throw new RuntimeException("프로필 정보 변경 실패 : 인증되지 않은 이메일");
        }

        changeLocalAccountEmail.changeLocalAccountEmail(
                localAccount.getAccountId(),
                newEmail);
    }
}
