package csct3434.ipo.service;

import csct3434.ipo.web.domain.IPO.IPO;
import csct3434.ipo.web.domain.IPO.IPORepository;
import csct3434.ipo.web.domain.Member.Member;
import csct3434.ipo.web.domain.Member.MemberRepository;
import csct3434.ipo.web.domain.Portfolio.Portfolio;
import csct3434.ipo.web.domain.Portfolio.PortfolioRepository;
import csct3434.ipo.web.dto.PortfolioSaveRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final IPORepository ipoRepository;
    private final MemberRepository memberRepository;

    public Portfolio save(Portfolio portfolio) {
        Portfolio savedPortfolio = portfolioRepository.save(portfolio);
        portfolio.getMember().addPortfolio(savedPortfolio);
        return savedPortfolio;
    }

    public Long createPortfolio(PortfolioSaveRequestDto requestDto) {
        IPO ipo = ipoRepository.findByStockCode(requestDto.getStockCode())
                .orElseThrow(() -> new IllegalArgumentException("해당 IPO가 없습니다. stockCode=" + requestDto.getStockCode()));

        Member member = memberRepository.findById(requestDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당 Member가 없습니다. memberId=" + requestDto.getMemberId()));

        Portfolio portfolio = Portfolio.createPortfolio(member, ipo, requestDto.getSharesCnt(), requestDto.getProfit());
        member.addPortfolio(portfolio);
        memberRepository.save(member);

        return portfolio.getId();
    }

}
