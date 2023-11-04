package com.alzzaipo.hexagonal.common.util;

import com.alzzaipo.hexagonal.common.Uid;

import java.security.SecureRandom;

public class UidGenerator {

    private static final SecureRandom random = new SecureRandom();

    public static Uid generate() {
        return new Uid(random.nextLong(1L, Long.MAX_VALUE));
    }
}
