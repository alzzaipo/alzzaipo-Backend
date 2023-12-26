package com.alzzaipo.common;

import com.alzzaipo.common.exception.CustomException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.f4b6a3.tsid.Tsid;
import com.github.f4b6a3.tsid.TsidCreator;
import java.util.Objects;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
public class Id {

	@JsonProperty("id")
	private long id;

	public Id(Long id) {
		if (id <= 0) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "ID 값 오류");
		}
		this.id = id;
	}

	public static Id generate() {
		return new Id(TsidCreator.getTsid().toLong());
	}

	public static Id fromString(String id) {
		return new Id(Tsid.decode(id, 62).toLong());
	}

	@Override
	public String toString() {
		return Tsid.from(this.id).encode(62);
	}

	public Long get() {
		return id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Id u = (Id) o;
		return Objects.equals(id, u.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
