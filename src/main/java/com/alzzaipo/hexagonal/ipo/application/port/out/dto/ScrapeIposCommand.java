package com.alzzaipo.hexagonal.ipo.application.port.out.dto;

import lombok.Getter;

@Getter
public class ScrapeIposCommand {

    private final int pageFrom;
    private final int pageTo;

    public ScrapeIposCommand(int pageFrom, int to) {
        this.pageFrom = pageFrom;
        this.pageTo = to;

        selfValidate(pageFrom, to);
    }

    private void selfValidate(int from, int to) {
        requirePositive(from);
        requirePositive(to);

        if(from > to) {
            throw new IllegalArgumentException();
        }
    }

    private void requirePositive(int number) {
        if(number <= 0) {
            throw new IllegalArgumentException();
        }
    }
}
