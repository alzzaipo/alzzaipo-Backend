package com.alzzaipo.notification.application.service.email;

import com.alzzaipo.common.Uid;
import com.alzzaipo.notification.application.port.dto.EmailNotificationStatus;
import com.alzzaipo.notification.application.port.in.email.FindEmailNotificationStatusQuery;
import com.alzzaipo.notification.application.port.out.email.FindNotificationEmailPort;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindEmailNotificationService implements FindEmailNotificationStatusQuery {

	private final FindNotificationEmailPort findNotificationEmailPort;

	@Override
	public EmailNotificationStatus find(Uid memberUID) {
		Optional<String> notificationEmail = findNotificationEmailPort.findNotificationEmail(memberUID);
		return new EmailNotificationStatus(notificationEmail.isPresent(), notificationEmail.orElse(""));
	}

}
