package com.alzzaipo.portfolio.application.dto;

import com.alzzaipo.common.Uid;
import com.alzzaipo.common.exception.CustomException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RegisterPortfolioCommand {

    private final Uid memberUID;
    private final int stockCode;
    private final int sharesCnt;
    private final Long profit;
    private final String agents;
    private final String memo;

    public RegisterPortfolioCommand(Uid memberUID, int stockCode, int sharesCnt, Long profit, String agents, String memo) {
        this.memberUID = memberUID;
        this.stockCode = stockCode;
        this.sharesCnt = sharesCnt;
        this.profit = profit;
        this.agents = agents;
        this.memo = memo;

        selfValidate();
    }

    private void selfValidate() {
        requireZeroOrPositive("종목 코드", stockCode);
        requireZeroOrPositive("배정 수량", sharesCnt);
        verifyMemoLength();
    }

    private void requireZeroOrPositive(String propertyName, int value) {
        if (value < 0) {
            throw new CustomException(HttpStatus.BAD_REQUEST, propertyName + " 오류 : 음수 불가");
        }
    }

    private void verifyMemoLength() {
        if (memo.length() > 200) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "메모 글자수 초과 : 최대 200자");
        }
    }
}
