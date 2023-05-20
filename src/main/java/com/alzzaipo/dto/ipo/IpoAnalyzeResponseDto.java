package com.alzzaipo.dto.ipo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class IpoAnalyzeResponseDto {

    private int averageProfitRate;
    private List<IpoDto> ipoDtoList;
}
