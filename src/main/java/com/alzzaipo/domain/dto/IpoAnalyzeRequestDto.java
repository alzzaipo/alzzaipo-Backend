package com.alzzaipo.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IpoAnalyzeRequestDto {
    @JsonProperty("from")
    private int from;
    @JsonProperty("to")
    private int to;
    @JsonProperty("minCompetitionRate")
    private int minCompetitionRate;
    @JsonProperty("minLockupRate")
    private int minLockupRate;

    public IpoAnalyzeRequestDto(int from, int to, int minCompetitionRate, int minLockupRate) {
        this.from = from;
        this.to = to;
        this.minCompetitionRate = minCompetitionRate;
        this.minLockupRate = minLockupRate;
    }
}
