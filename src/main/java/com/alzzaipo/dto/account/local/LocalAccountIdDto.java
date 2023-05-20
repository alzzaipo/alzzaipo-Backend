package com.alzzaipo.dto.account.local;

import lombok.Getter;

import java.beans.ConstructorProperties;

@Getter
public class LocalAccountIdDto {
    private String accountId;

    @ConstructorProperties("{accountId}")
    public LocalAccountIdDto(String accountId) {
        this.accountId = accountId;
    }
}
