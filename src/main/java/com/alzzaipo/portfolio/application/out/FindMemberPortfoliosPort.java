package com.alzzaipo.portfolio.application.out;

import com.alzzaipo.common.Uid;
import com.alzzaipo.portfolio.domain.Portfolio;

import java.util.List;

public interface FindMemberPortfoliosPort {

    List<Portfolio> findMemberPortfolios(Uid memberUID);
}
