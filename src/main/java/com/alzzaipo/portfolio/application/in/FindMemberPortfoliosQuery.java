package com.alzzaipo.portfolio.application.in;

import com.alzzaipo.common.Uid;
import com.alzzaipo.portfolio.application.dto.MemberPortfolioSummary;

public interface FindMemberPortfoliosQuery {

    MemberPortfolioSummary findMemberPortfolios(Uid memberUID);
}
