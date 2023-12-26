package com.alzzaipo.notification.application.port.out.email;

import com.alzzaipo.common.Id;
import java.util.Optional;

public interface FindNotificationEmailPort {

	Optional<String> findNotificationEmail(Id memberId);
}
