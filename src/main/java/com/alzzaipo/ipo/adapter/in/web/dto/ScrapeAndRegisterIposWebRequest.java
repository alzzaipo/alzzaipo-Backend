package com.alzzaipo.ipo.adapter.in.web.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ScrapeAndRegisterIposWebRequest {

	@Min(value = 1)
	private int pageFrom;

	@Min(value = 1)
	private int pageTo;
}
