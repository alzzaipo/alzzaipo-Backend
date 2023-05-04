package com.alzzaipo.domain.dto;

import lombok.Getter;

import java.beans.ConstructorProperties;

@Getter
public class AccountIdDto {
    private String accountId;

    @ConstructorProperties("{accountId}")
    public AccountIdDto(String accountId) {
        this.accountId = accountId;
    }
}
