package com.alzzaipo.domain.dto;

import lombok.Getter;

import java.beans.ConstructorProperties;

@Getter
public class EmailDto {
    private final String email;

    @ConstructorProperties("{email}")   // 'JSON parse error; Cannot construct instance of ~' 에러 처리
    public EmailDto(String email) {
        this.email = email;
    }
}