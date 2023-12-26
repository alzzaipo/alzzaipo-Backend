package com.alzzaipo.notification.application.port.out.email;

import com.alzzaipo.common.Id;

public interface CheckMemberSubscriptionExistsPort {

	boolean checkSubscription(Id memberId);
}
