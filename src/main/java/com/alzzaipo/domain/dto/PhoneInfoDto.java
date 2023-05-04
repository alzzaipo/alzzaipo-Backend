package com.alzzaipo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PhoneInfoDto {
    private String countryCode;
    private String phoneNumber;
}
