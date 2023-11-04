package com.alzzaipo.hexagonal.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;

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
            throw new IllegalArgumentException("UID Null");
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
}
