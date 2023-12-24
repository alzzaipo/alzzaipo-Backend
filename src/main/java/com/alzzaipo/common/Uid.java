package com.alzzaipo.common;

import com.alzzaipo.common.exception.CustomException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.f4b6a3.tsid.Tsid;
import com.github.f4b6a3.tsid.TsidCreator;
import java.util.Objects;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
public class Uid {

	@JsonProperty("uid")
	private long uid;

	public Uid(Long uid) {
		if (uid <= 0) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "UID 값 오류");
		}
		this.uid = uid;
	}

	public static Uid generate() {
		return new Uid(TsidCreator.getTsid().toLong());
	}

	public static Uid fromString(String uid) {
		return new Uid(Tsid.decode(uid, 62).toLong());
	}

	@Override
	public String toString() {
		return Tsid.from(this.uid).encode(62);
	}

	public Long get() {
		return uid;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Uid u = (Uid) o;
		return Objects.equals(uid, u.uid);
	}

	@Override
	public int hashCode() {
		return Objects.hash(uid);
	}
}
