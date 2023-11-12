package com.alzzaipo.ipo.application.port.out.dto;

import com.alzzaipo.common.exception.CustomException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

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
        requirePositive("from", from);
        requirePositive("to", to);

        if (from > to) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "from이 to보다 큽니다.");
        }
    }

    private void requirePositive(String parameter, int number) {
        if (number <= 0) {
            throw new CustomException(HttpStatus.BAD_REQUEST, parameter + " : 0 이하 불가");
        }
    }
}
