package com.alzzaipo.hexagonal.portfolio.application.out;

import com.alzzaipo.hexagonal.common.Uid;
import com.alzzaipo.hexagonal.portfolio.domain.Portfolio;

import java.util.List;

public interface FindMemberPortfoliosPort {

    List<Portfolio> findMemberPortfolios(Uid memberUID);
}
