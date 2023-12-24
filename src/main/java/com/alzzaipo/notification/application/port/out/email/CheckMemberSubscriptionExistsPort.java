package com.alzzaipo.notification.application.port.out.email;

import com.alzzaipo.common.Uid;

public interface CheckMemberSubscriptionExistsPort {

	boolean checkSubscription(Uid memberId);
}
