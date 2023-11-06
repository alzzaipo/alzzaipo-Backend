package com.alzzaipo.controller;

import com.alzzaipo.config.MemberPrincipal;
import com.alzzaipo.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/portfolio")
@RestController
public class PortfolioController {

    private final PortfolioService portfolioService;

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@AuthenticationPrincipal MemberPrincipal memberInfo, @RequestParam("portfolioId") Long portfolioId) {
        portfolioService.delete(memberInfo.getMemberId(), portfolioId);
        return ResponseEntity.ok().body("포트폴리오 삭제 완료");
    }

}
