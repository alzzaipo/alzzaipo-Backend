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

        selfValidate();
    }

    private void selfValidate() {
        if (pageFrom <= 0) {
            throw new CustomException(HttpStatus.BAD_REQUEST, " pageFrom : 0 이하 불가");
        }

        if (pageTo <= 0) {
            throw new CustomException(HttpStatus.BAD_REQUEST, " pageTo : 0 이하 불가");
        }

        if (pageFrom > pageTo) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "from이 to보다 큽니다.");
        }
    }
}
