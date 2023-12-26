package com.alzzaipo.notification.application.service.scheduled;

import com.alzzaipo.common.email.port.out.smtp.SendCustomEmailPort;
import com.alzzaipo.ipo.application.port.out.FindNotListedIposPort;
import com.alzzaipo.ipo.domain.Ipo;
import com.alzzaipo.notification.application.port.out.criterion.FindAllNotificationCriterionPort;
import com.alzzaipo.notification.application.port.out.email.FindNotificationEmailPort;
import com.alzzaipo.notification.domain.criterion.NotificationCriterion;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendEmailNotificationService {

	private final FindNotListedIposPort findNotListedIposPort;
	private final FindAllNotificationCriterionPort findAllNotificationCriterionPort;
	private final FindNotificationEmailPort findNotificationEmailPort;
	private final SendCustomEmailPort sendCustomEmailPort;

	@Scheduled(cron = "0 0 10 ? * MON-FRI")
	private void scheduledTask() {
		List<Ipo> todaySubscriptionOpenedIpos = findTodaySubscriptionOpenedIpos();

		List<NotificationCriterion> allNotificationCriterion
			= findAllNotificationCriterionPort.findAllNotificationCriterion();

		sendEmailForIpos(todaySubscriptionOpenedIpos, allNotificationCriterion);
	}

	private List<Ipo> findTodaySubscriptionOpenedIpos() {
		return findNotListedIposPort.findNotListedIpos()
			.stream()
			.filter(ipo -> LocalDate.now().isEqual(ipo.getSubscribeStartDate()))
			.collect(Collectors.toList());
	}

	private void sendEmailForIpos(List<Ipo> ipos, List<NotificationCriterion> criteria) {
		for (Ipo ipo : ipos) {
			List<String> targetEmails = criteria.stream()
				.filter(notificationCriterion -> isCriterionMet(ipo, notificationCriterion))
				.map(NotificationCriterion::getMemberId)
				.distinct()
				.map(findNotificationEmailPort::findNotificationEmail)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toList());

			sendEmailNotification(targetEmails, ipo);
		}
	}

	private void sendEmailNotification(List<String> emails, Ipo ipo) {
		String subject = "[알짜공모주] 공모주 청약 알림: " + ipo.getStockName();
		String text = generateEmailText(ipo);
		emails.parallelStream().forEach(email -> sendCustomEmailPort.send(email, subject, text));
	}

	private String generateEmailText(Ipo ipo) {
		StringBuilder builder = new StringBuilder();

		builder.append("*새로운 공모주가 등장했어요!*\n")
			.append("\n[공모주명] : ").append(ipo.getStockName())
			.append("\n[기관경쟁률] : ").append(ipo.getCompetitionRate())
			.append("\n[의무확약비율] : ").append(ipo.getLockupRate())
			.append("\n[희망공모가] : ").append(ipo.getExpectedOfferingPriceMin()).append("~")
			.append(ipo.getExpectedOfferingPriceMax())
			.append("\n[최종공모가] : ").append(ipo.getFixedOfferingPrice())
			.append("\n[청약주간사] : ").append(ipo.getAgents())
			.append("\n[청약일] : ").append(formatDate(ipo.getSubscribeStartDate())).append(" ~ ")
			.append(formatDate(ipo.getSubscribeEndDate()))
			.append("\n[상장일] : ").append(formatDate(ipo.getListedDate()));

		return builder.toString();
	}

	private String formatDate(LocalDate date) {
		return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}

	private boolean isCriterionMet(Ipo ipo, NotificationCriterion notificationCriterion) {
		return ipo.getCompetitionRate() >= notificationCriterion.getMinCompetitionRate() &&
			ipo.getLockupRate() >= notificationCriterion.getMinLockupRate();
	}
}
