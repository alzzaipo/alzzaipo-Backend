package com.alzzaipo.member.adapter.in.web.dto;

import com.alzzaipo.common.Id;
import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.email.domain.EmailVerificationCode;
import com.alzzaipo.member.application.port.in.dto.UpdateMemberProfileCommand;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateMemberProfileWebRequest {

    @NotBlank
    private String nickname;

    @NotBlank
    private String email;

    @NotBlank
    private String verificationCode;

    public UpdateMemberProfileCommand toCommand(Id memberId) {
        return new UpdateMemberProfileCommand(
            memberId,
            new Email(email),
            nickname,
            new EmailVerificationCode(verificationCode));
    }

}
