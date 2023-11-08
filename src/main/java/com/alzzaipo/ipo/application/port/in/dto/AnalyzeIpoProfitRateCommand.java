package com.alzzaipo.ipo.application.port.in.dto;

import lombok.Getter;

@Getter
public class AnalyzeIpoProfitRateCommand {

    private final int yearFrom;
    private final int yearTo;
    private final int minCompetitionRate;
    private final int minLockupRate;

    public AnalyzeIpoProfitRateCommand(int yearFrom, int yearTo, int minCompetitionRate, int minLockupRate) {
        this.yearFrom = yearFrom;
        this.yearTo = yearTo;
        this.minCompetitionRate = minCompetitionRate;
        this.minLockupRate = minLockupRate;

        validateYear(yearFrom, yearTo);
        validateCompetitionRate(minCompetitionRate);
        validateMinLockupRate(minLockupRate);
    }

    private void validateYear(int yearFrom, int yearTo) {
        if (yearFrom < 0 || yearTo < 0 || yearFrom > yearTo) {
            throw new IllegalArgumentException();
        }
    }

    private void validateCompetitionRate(int minCompetitionRate) {
        if (minCompetitionRate < 0) {
            throw new IllegalArgumentException();
        }
    }

    private void validateMinLockupRate(int minLockupRate) {
        if (minLockupRate < 0 || minLockupRate > 100) {
            throw new IllegalArgumentException();
        }
    }

}
