package com.alzzaipo.controller;

import com.alzzaipo.config.MemberPrincipal;
import com.alzzaipo.dto.portfolio.PortfolioListDto;
import com.alzzaipo.dto.portfolio.PortfolioUpdateRequestDto;
import com.alzzaipo.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/portfolio")
@RestController
public class PortfolioController {

    private final PortfolioService portfolioService;

    @GetMapping("/list")
    public ResponseEntity<PortfolioListDto> getMemberPortfolios(@AuthenticationPrincipal MemberPrincipal memberInfo) {
        PortfolioListDto portfolioListDto = portfolioService.getPortfolioListDto(memberInfo.getMemberId());
        return ResponseEntity.ok(portfolioListDto);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateMemberPortfolio(@AuthenticationPrincipal MemberPrincipal memberInfo,
                                                        @RequestBody PortfolioUpdateRequestDto portfolioUpdateRequestDto) {
        portfolioService.update(memberInfo.getMemberId(), portfolioUpdateRequestDto);
        return ResponseEntity.ok().body("포트폴리오 수정 완료");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@AuthenticationPrincipal MemberPrincipal memberInfo, @RequestParam("portfolioId") Long portfolioId) {
        portfolioService.delete(memberInfo.getMemberId(), portfolioId);
        return ResponseEntity.ok().body("포트폴리오 삭제 완료");
    }

}
