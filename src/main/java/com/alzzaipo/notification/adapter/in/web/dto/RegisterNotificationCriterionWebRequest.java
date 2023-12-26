package com.alzzaipo.notification.adapter.in.web.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@NoArgsConstructor
public class RegisterNotificationCriterionWebRequest {

	@Min(value = 0)
	private int competitionRate;

	@Range(min = 0, max = 100)
	private int lockupRate;
}
