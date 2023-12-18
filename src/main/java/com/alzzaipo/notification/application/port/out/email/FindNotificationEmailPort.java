package com.alzzaipo.notification.application.port.out.email;

import com.alzzaipo.common.Uid;
import java.util.Optional;

public interface FindNotificationEmailPort {

	Optional<String> findNotificationEmail(Uid memberUID);
}
