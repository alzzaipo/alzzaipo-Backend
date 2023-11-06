package com.alzzaipo.hexagonal.portfolio.adapter.in.web;

import com.alzzaipo.hexagonal.common.MemberPrincipal;
import com.alzzaipo.hexagonal.portfolio.adapter.in.web.dto.RegisterPortfolioWebRequest;
import com.alzzaipo.hexagonal.portfolio.application.dto.RegisterPortfolioCommand;
import com.alzzaipo.hexagonal.portfolio.application.in.FindMemberPortfoliosQuery;
import com.alzzaipo.hexagonal.portfolio.application.in.RegisterPortfolioUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/portfolio")
@RequiredArgsConstructor
public class NewPortfolioController {

    private final RegisterPortfolioUseCase registerPortfolioUseCase;
    private final FindMemberPortfoliosQuery findMemberPortfoliosQuery;

    @PostMapping("/create")
    public ResponseEntity<String> createPortfolio(@AuthenticationPrincipal MemberPrincipal principal,
                                                  @RequestBody RegisterPortfolioWebRequest dto) {
        RegisterPortfolioCommand command = toRegisterPortfolioCommand(principal, dto);

        registerPortfolioUseCase.registerPortfolio(command);

        return ResponseEntity.ok().body("포트폴리오 생성 완료");
    }

    private RegisterPortfolioCommand toRegisterPortfolioCommand(MemberPrincipal principal, RegisterPortfolioWebRequest registerPortfolioWebRequest) {
        return new RegisterPortfolioCommand(
                principal.getMemberUID(),
                registerPortfolioWebRequest.getStockCode(),
                registerPortfolioWebRequest.getSharesCnt(),
                registerPortfolioWebRequest.getProfit(),
                registerPortfolioWebRequest.getAgents(),
                registerPortfolioWebRequest.getMemo());
    }

}
