package com.alzzaipo.portfolio.application.port.in;

import com.alzzaipo.common.Id;
import com.alzzaipo.portfolio.application.dto.MemberPortfolioSummary;

public interface FindMemberPortfoliosQuery {

    MemberPortfolioSummary findMemberPortfolios(Id memberId);
}
