package com.alzzaipo.common.email.domain;

import com.alzzaipo.common.exception.CustomException;
import java.util.Objects;
import org.springframework.http.HttpStatus;

public class Email {

	private static final String regexPattern = "^(?=.{1,256}$)[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

	private final String email;

	public Email(String email) {
		this.email = email;
		validateFormat(email);
	}

	private void validateFormat(String email) {
		if(!java.util.regex.Pattern.matches(regexPattern, email)) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "이메일 형식 오류");
		}
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
