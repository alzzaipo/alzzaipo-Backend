package com.alzzaipo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PhoneInfoDto {
    private String countryCode;
    private String phoneNumber;
}
