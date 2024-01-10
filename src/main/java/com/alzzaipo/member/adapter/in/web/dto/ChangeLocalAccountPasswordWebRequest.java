package com.alzzaipo.member.adapter.in.web.dto;

import com.alzzaipo.common.Id;
import com.alzzaipo.member.application.port.in.dto.ChangeLocalAccountPasswordCommand;
import com.alzzaipo.member.domain.account.local.LocalAccountPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChangeLocalAccountPasswordWebRequest {

	@NotBlank
	@Size(min = 8, max = 16)
	private String currentPassword;

	@NotBlank
	@Size(min = 8, max = 16)
	private String newPassword;

	public ChangeLocalAccountPasswordCommand toCommand(Id memberId) {
		return new ChangeLocalAccountPasswordCommand(
			memberId,
			new LocalAccountPassword(currentPassword),
			new LocalAccountPassword(newPassword));
	}
}
