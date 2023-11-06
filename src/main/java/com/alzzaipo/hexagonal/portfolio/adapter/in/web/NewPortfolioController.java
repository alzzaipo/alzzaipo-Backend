package com.alzzaipo.hexagonal.portfolio.adapter.in.web;

import com.alzzaipo.hexagonal.common.MemberPrincipal;
import com.alzzaipo.hexagonal.common.Uid;
import com.alzzaipo.hexagonal.portfolio.adapter.in.web.dto.RegisterPortfolioWebRequest;
import com.alzzaipo.hexagonal.portfolio.adapter.in.web.dto.UpdatePortfolioWebRequest;
import com.alzzaipo.hexagonal.portfolio.application.dto.*;
import com.alzzaipo.hexagonal.portfolio.application.in.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/portfolio")
@RequiredArgsConstructor
public class NewPortfolioController {

    private final RegisterPortfolioUseCase registerPortfolioUseCase;
    private final FindMemberPortfoliosQuery findMemberPortfoliosQuery;
    private final UpdatePortfolioUseCase updatePortfolioUseCase;
    private final DeletePortfolioUseCase deletePortfolioUseCase;
    private final FindPortfolioQuery findPortfolioQuery;

    @PostMapping("/create")
    public ResponseEntity<String> createPortfolio(@AuthenticationPrincipal MemberPrincipal principal,
                                                  @RequestBody RegisterPortfolioWebRequest dto) {
        RegisterPortfolioCommand command = toRegisterPortfolioCommand(principal, dto);

        registerPortfolioUseCase.registerPortfolio(command);

        return ResponseEntity.ok().body("포트폴리오 생성 완료");
    }

    @GetMapping
    public ResponseEntity<PortfolioView> find(@AuthenticationPrincipal MemberPrincipal principal,
                                              @RequestParam("uid") Long portfolioUID) {
        FindPortfolioCommand command = new FindPortfolioCommand(
                principal.getMemberUID(),
                new Uid(portfolioUID));

        PortfolioView portfolio = findPortfolioQuery.findPortfolio(command);

        return ResponseEntity.ok().body(portfolio);
    }

    @GetMapping("/list")
    public ResponseEntity<MemberPortfolioSummary> findPortfolios(@AuthenticationPrincipal MemberPrincipal principal) {
        MemberPortfolioSummary memberPortfolioSummary
                = findMemberPortfoliosQuery.findMemberPortfolios(principal.getMemberUID());

        return ResponseEntity.ok().body(memberPortfolioSummary);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateMemberPortfolio(@AuthenticationPrincipal MemberPrincipal principal,
                                                        @RequestBody UpdatePortfolioWebRequest dto) {
        UpdatePortfolioCommand command = toUpdateMemberPortfolioCommand(principal, dto);

        updatePortfolioUseCase.updatePortfolio(command);

        return ResponseEntity.ok().body("포트폴리오 수정 완료");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@AuthenticationPrincipal MemberPrincipal principal,
                                         @RequestParam("uid") Long portfolioUID) {
        DeletePortfolioCommand command = new DeletePortfolioCommand(
                principal.getMemberUID(),
                new Uid(portfolioUID));

        deletePortfolioUseCase.deletePortfolio(command);

        return ResponseEntity.ok().body("포트폴리오 삭제 완료");
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

    private UpdatePortfolioCommand toUpdateMemberPortfolioCommand(MemberPrincipal principal, UpdatePortfolioWebRequest dto) {
        return new UpdatePortfolioCommand(
                new Uid(dto.getUid()),
                principal.getMemberUID(),
                dto.getStockCode(),
                dto.getSharesCnt(),
                dto.getProfit(),
                dto.getAgents(),
                dto.getMemo());
    }
}
