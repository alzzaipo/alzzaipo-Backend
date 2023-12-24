package com.alzzaipo.notification.adapter.in.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@NoArgsConstructor
public class UpdateNotificationCriterionWebRequest {

	@NotBlank
	private String uid;

	@Min(value = 0)
	private int competitionRate;

	@Range(min = 0, max = 100)
	private int lockupRate;
}
