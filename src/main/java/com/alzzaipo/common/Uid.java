package com.alzzaipo.common;

import com.alzzaipo.common.exception.CustomException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Objects;

@NoArgsConstructor
public class Uid {

    @JsonProperty("uid")
    private Long uid;

    public Uid(Long uid) {
        this.uid = uid;
        selfValidate();
    }

    private void selfValidate() {
        if (uid == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "UID 오류 : null");
        }

        if (uid <= 0) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "UID 오류 : 0 이하 불가");
        }
    }

    public Long get() {
        return uid;
    }

    public String toJson() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(this);
        } catch (Exception e) {
            throw new RuntimeException("Error converting Uid to JSON", e);
        }
    }

    public static Uid fromJson(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, Uid.class);
        } catch (Exception e) {
            throw new RuntimeException("Error converting JSON to Uid", e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Uid u = (Uid) o;
        return Objects.equals(uid, u.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid);
    }
}
