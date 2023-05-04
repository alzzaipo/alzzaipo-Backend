package com.alzzaipo.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IpoAnalyzeRequestDto {
    @JsonProperty("from")
    private int from;
    @JsonProperty("to")
    private int to;
    @JsonProperty("minCompetitionRate")
    private int minCompetitionRate;
    @JsonProperty("minLockupRate")
    private int minLockupRate;

}
