package com.alzzaipo.notification.application.port.out.email;

import com.alzzaipo.common.email.domain.Email;

public interface CheckNotificationEmailAvailablePort {

	boolean checkEmailAvailable(Email email);
}
