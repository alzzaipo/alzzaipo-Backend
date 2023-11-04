package com.alzzaipo.hexagonal.ipo.adapter.in.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ScrapeAndRegisterIposWebRequest {

    private String authorizationCode;
    private int pageFrom;
    private int pageTo;
}
