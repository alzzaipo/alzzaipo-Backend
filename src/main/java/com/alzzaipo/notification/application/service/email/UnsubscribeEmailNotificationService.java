package com.alzzaipo.notification.application.service.email;

import com.alzzaipo.common.Uid;
import com.alzzaipo.notification.application.port.in.email.UnsubscribeEmailNotificationUseCase;
import com.alzzaipo.notification.application.port.out.UnsubscribeEmailNotificationPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UnsubscribeEmailNotificationService implements UnsubscribeEmailNotificationUseCase {

	private final UnsubscribeEmailNotificationPort unsubscribeEmailNotificationPort;

	@Override
	public void unsubscribeEmailNotification(Uid memberUID) {
		unsubscribeEmailNotificationPort.unsubscribeEmailNotification(memberUID);
	}
}
