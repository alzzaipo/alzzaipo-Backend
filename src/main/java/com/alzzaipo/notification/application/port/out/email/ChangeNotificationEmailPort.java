package com.alzzaipo.notification.application.port.out.email;

public interface ChangeNotificationEmailPort {

	void changeEmail(Long memberId, String email);
}
