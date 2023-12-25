package com.alzzaipo.member.application.service.login;

import com.alzzaipo.common.LoginType;
import com.alzzaipo.common.Uid;
import com.alzzaipo.common.token.TokenUtil;
import com.alzzaipo.common.token.domain.TokenInfo;
import com.alzzaipo.member.application.port.in.account.local.LocalLoginUseCase;
import com.alzzaipo.member.application.port.in.dto.LocalLoginCommand;
import com.alzzaipo.member.application.port.in.dto.LoginResult;
import com.alzzaipo.common.token.application.port.out.SaveRefreshTokenPort;
import com.alzzaipo.member.application.port.out.account.local.FindLocalAccountByAccountIdPort;
import com.alzzaipo.member.application.port.out.dto.SecureLocalAccount;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocalLoginService implements LocalLoginUseCase {

	private final PasswordEncoder passwordEncoder;
	private final SaveRefreshTokenPort saveRefreshTokenPort;
	private final FindLocalAccountByAccountIdPort findLocalAccountByAccountIdPort;

	@Override
	public LoginResult handleLocalLogin(LocalLoginCommand command) {
		Optional<SecureLocalAccount> optionalLocalAccount
			= findLocalAccountByAccountIdPort.findLocalAccountByAccountId(command.getLocalAccountId());

		if (optionalLocalAccount.isPresent() && isPasswordValid(command, optionalLocalAccount.get())) {
			Uid memberId = optionalLocalAccount.get().getMemberUID();
			TokenInfo tokenInfo = TokenUtil.createToken(memberId, LoginType.LOCAL);
			saveRefreshTokenPort.save(tokenInfo.getRefreshToken(), memberId);
			return new LoginResult(true, tokenInfo);
		}
		return LoginResult.getFailedResult();
	}

	private boolean isPasswordValid(LocalLoginCommand command, SecureLocalAccount secureLocalAccount) {
		String userProvidedPassword = command.getLocalAccountPassword().get();
		String validEncryptedPassword = secureLocalAccount.getEncryptedAccountPassword();
		return passwordEncoder.matches(userProvidedPassword, validEncryptedPassword);
	}
}
