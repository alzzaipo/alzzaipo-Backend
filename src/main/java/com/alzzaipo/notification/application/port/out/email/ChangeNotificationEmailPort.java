package com.alzzaipo.notification.application.port.out.email;

import com.alzzaipo.common.Id;
import com.alzzaipo.common.email.domain.Email;

public interface ChangeNotificationEmailPort {

	void changeEmail(Id memberId, Email email);
}
