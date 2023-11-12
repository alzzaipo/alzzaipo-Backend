package com.alzzaipo.ipo.application.port.in.dto;

import com.alzzaipo.common.exception.CustomException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

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
        if (yearFrom < 0) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "from : 0 이상");
        }

        if (yearTo < 0) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "to : 0 이상");
        }

        if (yearFrom > yearTo) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "from이 to보다 큽니다.");
        }
    }

    private void validateCompetitionRate(int minCompetitionRate) {
        if (minCompetitionRate < 0) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "minCompetitionRate : 0 이상");
        }
    }

    private void validateMinLockupRate(int minLockupRate) {
        if (minLockupRate < 0 || minLockupRate > 100) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "minLockupRate : 0 이상 100 이하");

        }
    }

}
