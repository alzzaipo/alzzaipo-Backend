package com.alzzaipo.ipo.adapter.in.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ScrapeAndRegisterIposWebRequest {

    @NotBlank
    private String authorizationCode;

    @Min(value = 1)
    private int pageFrom;

    @Min(value = 1)
    private int pageTo;
}
