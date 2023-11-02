package com.alzzaipo.hexagonal.common;

import java.security.SecureRandom;

public class UidGenerator {

    private static final SecureRandom random = new SecureRandom();

    public static long generate() {
        return random.nextLong();
    }
}
