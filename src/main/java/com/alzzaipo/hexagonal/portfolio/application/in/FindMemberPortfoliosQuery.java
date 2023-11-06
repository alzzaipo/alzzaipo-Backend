package com.alzzaipo.hexagonal.portfolio.application.in;

import com.alzzaipo.hexagonal.common.Uid;
import com.alzzaipo.hexagonal.portfolio.application.dto.MemberPortfolioSummary;

public interface FindMemberPortfoliosQuery {

    MemberPortfolioSummary findMemberPortfolios(Uid memberUID);
}
