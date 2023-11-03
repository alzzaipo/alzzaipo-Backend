package com.alzzaipo.hexagonal.member.domain.LocalAccount;

import java.util.regex.Pattern;

public class LocalAccountId {

    private final String accountId;

    public LocalAccountId(String accountId) {
        this.accountId = accountId;

        validateFormat();
    }

    private void validateFormat() {
        String regex = "^[a-z0-9_-]{5,20}$";

        if (!Pattern.matches(regex, accountId)) {
            throw new IllegalArgumentException("아이디 형식 오류");
        }
    }

    public String get() {
        return accountId;
    }
}
