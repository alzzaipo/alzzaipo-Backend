package com.alzzaipo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class IpoAnalyzeResponseDto {

    private int averageProfitRate;
    private List<IpoDto> ipoDtoList;
}
