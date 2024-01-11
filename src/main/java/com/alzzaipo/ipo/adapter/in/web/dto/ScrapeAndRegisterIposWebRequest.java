package com.alzzaipo.ipo.adapter.in.web.dto;

import com.alzzaipo.ipo.application.port.out.dto.ScrapeIposCommand;
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

	public ScrapeIposCommand toCommand() {
		return new ScrapeIposCommand(pageFrom, pageTo);
	}
}
