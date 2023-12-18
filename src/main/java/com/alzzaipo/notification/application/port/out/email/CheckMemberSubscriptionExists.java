package com.alzzaipo.notification.application.port.out.email;

import com.alzzaipo.common.Uid;

public interface CheckMemberSubscriptionExists {

	boolean checkSubscription(Uid memberId);
}
