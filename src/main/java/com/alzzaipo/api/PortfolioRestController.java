package com.alzzaipo.api;

import com.alzzaipo.config.SessionManager;
import com.alzzaipo.domain.dto.PortfolioUpdateResponseDto;
import com.alzzaipo.service.PortfolioService;
import com.alzzaipo.domain.dto.PortfolioCreateRequestDto;
import com.alzzaipo.domain.dto.PortfolioListResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/portfolio")
@RestController
public class PortfolioRestController {

    private final PortfolioService portfolioService;
    private final SessionManager sessionManager;

    @GetMapping("/list")
    public ResponseEntity<List<PortfolioListResponseDto>> getMemberPortfolios(@AuthenticationPrincipal String accountId) {

        List<PortfolioListResponseDto> portfolioListResponseDtos =
                portfolioService.getMemberPortfolioListResponseDtos(accountId);

        return ResponseEntity.ok(portfolioListResponseDtos);
    }

    @PostMapping("/create")
    public ResponseEntity createMemberPortfolio(@AuthenticationPrincipal String accountId, PortfolioCreateRequestDto portfolioCreateRequestDto) {

        Boolean success = portfolioService.createMemberPortfolio(accountId, portfolioCreateRequestDto);

        if(success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity updateMemberPortfolio(@AuthenticationPrincipal String accountId, PortfolioUpdateResponseDto portfolioUpdateResponseDto) {
        Boolean success = portfolioService.updateMemberPortfolio(accountId, portfolioUpdateResponseDto);

        if(success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(@AuthenticationPrincipal String accountId, @RequestParam("portfolioId") Long portfolioId) {

        boolean isPortfolioDeleted  = portfolioService.deleteMemberPortfolio(accountId, portfolioId);

        if(isPortfolioDeleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
