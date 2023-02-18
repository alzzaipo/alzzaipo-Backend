package csct3434.ipo.web.domain.Portfolio;

import csct3434.ipo.web.domain.BaseTimeEntity;
import csct3434.ipo.web.domain.IPO.IPO;
import csct3434.ipo.web.domain.Member.Member;
import csct3434.ipo.web.dto.PortfolioListDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Getter
@NoArgsConstructor
@Entity
public class Portfolio extends BaseTimeEntity {
    @Id @GeneratedValue
    @Column(name = "portfolio_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ipo_id")
    private IPO ipo;

    private int sharesCnt;

    private int profit;

    private int profitRate;

    @Column(columnDefinition = "TEXT")
    private String memo;

    public static Portfolio createPortfolio(Member member, IPO ipo, int sharesCnt, int profit) {
        Portfolio portfolio = new Portfolio();

        portfolio.member = member;
        portfolio.ipo = ipo;
        portfolio.sharesCnt = sharesCnt;
        portfolio.profit = profit;
        portfolio.profitRate = calcInitialProfitRateOf(portfolio);

        return portfolio;
    }

    public void changeAllocatedSharesCnt(int sharesCnt) {
        this.sharesCnt = sharesCnt;
    }

    public int changeProfit(int profit) {
        this.profit = profit;
        this.profitRate = calcProfitRate();
        return profit;
    }

    private int calcProfitRate() {
        int profitPerShare = this.profit / this.sharesCnt;
        int fixedOfferingPrice = this.ipo.getFixedOfferingPrice();

        return ((profitPerShare - fixedOfferingPrice) * 100 / fixedOfferingPrice);
    }

    private static int calcInitialProfitRateOf(Portfolio portfolio) {
        int profit = portfolio.getProfit();
        int sharesCnt = portfolio.getSharesCnt();
        int profitPerShare = profit / sharesCnt;
        int fixedOfferingPrice = portfolio.getIpo().getFixedOfferingPrice();

        return ((profitPerShare - fixedOfferingPrice) * 100 / fixedOfferingPrice);
    }

    public void setMember(Member member) {
        this.member = member;
    }

    @Transactional(readOnly = true)
    public PortfolioListDto toDto() {
        PortfolioListDto portfolioListDto = PortfolioListDto.builder()
                .stockName(this.ipo.getStockName())
                .subscribeStartDate(this.ipo.getSubscribeStartDate())
                .subscribeEndDate(this.ipo.getSubscribeEndDate())
                .listedDate(this.ipo.getListedDate())
                .fixedOfferingPrice(this.ipo.getFixedOfferingPrice())
                .agents(this.ipo.getAgents())
                .sharesCnt(this.sharesCnt)
                .profit(this.profit)
                .profit(this.profitRate)
                .memo(this.memo)
                .build();

        return portfolioListDto;
    }
}
