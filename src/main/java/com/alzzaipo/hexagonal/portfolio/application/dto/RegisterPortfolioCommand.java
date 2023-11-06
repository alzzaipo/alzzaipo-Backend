package com.alzzaipo.hexagonal.portfolio.application.dto;

import com.alzzaipo.hexagonal.common.Uid;
import lombok.Getter;

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
            throw new IllegalArgumentException(propertyName + " 오류 : 음수 불가");
        }
    }

    private void verifyMemoLength() {
        if (memo.length() > 200) {
            throw new IllegalArgumentException("메모 글자수 초과 : 최대 200자");
        }
    }
}
