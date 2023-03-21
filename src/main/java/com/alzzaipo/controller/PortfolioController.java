package com.alzzaipo.controller;

import com.alzzaipo.domain.dto.PortfolioCreateRequestDto;
import com.alzzaipo.domain.dto.PortfolioListResponseDto;
import com.alzzaipo.domain.dto.PortfolioUpdateRequestDto;
import com.alzzaipo.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/portfolio")
@RestController
public class PortfolioController {

    private final PortfolioService portfolioService;

    @GetMapping("/list")
    public ResponseEntity<List<PortfolioListResponseDto>> getMemberPortfolios(@AuthenticationPrincipal String accountId) {

        List<PortfolioListResponseDto> portfolioListResponseDtos =
                portfolioService.getMemberPortfolioList(accountId);

        return ResponseEntity.ok(portfolioListResponseDtos);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createMemberPortfolio(@AuthenticationPrincipal String accountId,
                                                        @RequestBody PortfolioCreateRequestDto portfolioCreateRequestDto) {
        portfolioService.createMemberPortfolio(accountId, portfolioCreateRequestDto);
        return ResponseEntity.ok().body("포트폴리오 생성 완료");
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateMemberPortfolio(@AuthenticationPrincipal String accountId,
                                                        @RequestBody PortfolioUpdateRequestDto portfolioUpdateRequestDto) {
        portfolioService.updateMemberPortfolio(accountId, portfolioUpdateRequestDto);
        return ResponseEntity.ok().body("포트폴리오 수정 완료");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@AuthenticationPrincipal String accountId, @RequestParam("portfolioId") Long portfolioId) {
        portfolioService.deleteMemberPortfolio(accountId, portfolioId);
        return ResponseEntity.ok().body("포트폴리오 삭제 완료");
    }

}
