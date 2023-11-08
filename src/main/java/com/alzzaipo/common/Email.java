package com.alzzaipo.common;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Email {

    private final String email;

    public String get() {
        return email;
    }

    public Email(String email) {
        this.email = email;

        validateFormat();
    }

    private void validateFormat() {
        String regex = "^(?=.{1,256}$)[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("이메일 형식 오류");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email e = (Email) o;
        return Objects.equals(this.email, e.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
