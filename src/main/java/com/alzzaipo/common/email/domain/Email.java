package com.alzzaipo.common.email.domain;

import jakarta.validation.constraints.Pattern;
import java.util.Objects;

public class Email {

	@Pattern(message = "이메일 형식 오류", regexp = "^(?=.{1,256}$)[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
	private final String email;

	public Email(String email) {
		this.email = email;
	}

	public String get() {
		return email;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Email e = (Email) o;
		return Objects.equals(this.email, e.email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(email);
	}
}
