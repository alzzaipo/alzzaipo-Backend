package com.alzzaipo.hexagonal.common;

public class Uid {

    private final Long uid;

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

}
