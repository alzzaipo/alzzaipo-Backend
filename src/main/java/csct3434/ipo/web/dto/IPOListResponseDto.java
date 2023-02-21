package csct3434.ipo.web.dto;

import lombok.Getter;

@Getter
public class IPOListResponseDto {
    private String stockName;
    private int stockCode;

    public IPOListResponseDto(String stockName, int stockCode) {
        this.stockName = stockName;
        this.stockCode = stockCode;
    }
}
