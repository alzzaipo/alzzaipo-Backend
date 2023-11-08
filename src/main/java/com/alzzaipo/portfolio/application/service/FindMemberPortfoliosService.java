package com.alzzaipo.portfolio.application.service;

import com.alzzaipo.common.Uid;
import com.alzzaipo.member.application.port.out.member.FindMemberPort;
import com.alzzaipo.portfolio.application.dto.MemberPortfolioSummary;
import com.alzzaipo.portfolio.application.dto.PortfolioView;
import com.alzzaipo.portfolio.application.in.FindMemberPortfoliosQuery;
import com.alzzaipo.portfolio.application.out.FindMemberPortfoliosPort;
import com.alzzaipo.portfolio.domain.Portfolio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FindMemberPortfoliosService implements FindMemberPortfoliosQuery {

    private final FindMemberPort findMemberPort;
    private final FindMemberPortfoliosPort findMemberPortfoliosPort;

    @Override
    public MemberPortfolioSummary findMemberPortfolios(Uid memberUID) {
        findMemberPort.findMember(memberUID)
                .orElseThrow(() -> new RuntimeException("회원 조회 실패"));

        List<PortfolioView> portfolioList = findMemberPortfoliosPort.findMemberPortfolios(memberUID)
                .stream()
                .map(this::toViewModel)
                .collect(Collectors.toList());

        return new MemberPortfolioSummary(portfolioList);
    }

    public PortfolioView toViewModel(Portfolio portfolio) {
        return new PortfolioView(
                portfolio.getPortfolioUID().get(),
                portfolio.getStockName(),
                portfolio.getStockCode(),
                portfolio.getSharesCnt(),
                portfolio.getProfit(),
                portfolio.getProfitRate(),
                portfolio.getAgents(),
                portfolio.getMemo());
    }
}
