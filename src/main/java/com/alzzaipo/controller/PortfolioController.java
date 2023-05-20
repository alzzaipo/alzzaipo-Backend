package com.alzzaipo.controller;

import com.alzzaipo.dto.portfolio.PortfolioCreateRequestDto;
import com.alzzaipo.dto.portfolio.PortfolioResponseDto;
import com.alzzaipo.dto.portfolio.PortfolioUpdateRequestDto;
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
    public ResponseEntity<List<PortfolioResponseDto>> getMemberPortfolios(@AuthenticationPrincipal Long memberId) {
        List<PortfolioResponseDto> portfolioResponseDtos = portfolioService.getPortfolioListByMemberId(memberId);
        return ResponseEntity.ok(portfolioResponseDtos);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createMemberPortfolio(@AuthenticationPrincipal Long memberId,
                                                        @RequestBody PortfolioCreateRequestDto portfolioCreateRequestDto) {
        portfolioService.create(memberId, portfolioCreateRequestDto);
        return ResponseEntity.ok().body("포트폴리오 생성 완료");
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateMemberPortfolio(@AuthenticationPrincipal Long memberId,
                                                        @RequestBody PortfolioUpdateRequestDto portfolioUpdateRequestDto) {
        portfolioService.update(memberId, portfolioUpdateRequestDto);
        return ResponseEntity.ok().body("포트폴리오 수정 완료");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@AuthenticationPrincipal Long memberId, @RequestParam("portfolioId") Long portfolioId) {
        portfolioService.delete(memberId, portfolioId);
        return ResponseEntity.ok().body("포트폴리오 삭제 완료");
    }

}
