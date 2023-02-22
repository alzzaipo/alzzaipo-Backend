package com.alzzaipo.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class IPOAnalyzeRequestDto {
    private int from;
    private int to;
    private int competitionRate;
    private int lockupRate;

    @Builder
    public IPOAnalyzeRequestDto(int from, int to, int competitionRate, int lockupRate) {
        this.from = from;
        this.to = to;
        this.competitionRate = competitionRate;
        this.lockupRate = lockupRate;
    }

    @Override
    public String toString() {
        return "IPOAnalyzeRequestDto{" +
                "from=" + from +
                ", to=" + to +
                ", competitionRate=" + competitionRate +
                ", lockupRate=" + lockupRate +
                '}';
    }
}
