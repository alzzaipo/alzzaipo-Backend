package com.alzzaipo.web.api;

import com.alzzaipo.config.SessionConfig;
import com.alzzaipo.config.SessionManager;
import com.alzzaipo.service.PortfolioService;
import com.alzzaipo.web.dto.PortfolioCreateRequestDto;
import com.alzzaipo.web.dto.PortfolioListDto;
import com.alzzaipo.web.dto.PortfolioUpdateDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/portfolio")
@RestController
public class PortfolioRestController {

    private final PortfolioService portfolioService;
    private final SessionManager sessionManager;

    @GetMapping("/list")
    public ResponseEntity<List<PortfolioListDto>> getMemberPortfolios(HttpSession session) {
        if(!sessionManager.verifySession(session)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ArrayList<>());
        }

        Long memberId = getMemberId(session);
        List<PortfolioListDto> memberPortfolios = portfolioService.getMemberPortfolioListDtos(memberId);

        return ResponseEntity.ok(memberPortfolios);
    }

    @PostMapping("/create")
    public ResponseEntity createUserPortfolio(HttpSession session, PortfolioCreateRequestDto portfolioCreateRequestDto) {
        if(!sessionManager.verifySession(session)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long memberId = getMemberId(session);
        Boolean success = portfolioService.createMemberPortfolio(memberId, portfolioCreateRequestDto);

        if(success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity updateUserPortfolio(HttpSession session, PortfolioUpdateDto portfolioUpdateDto) {
        if(!sessionManager.verifySession(session)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long memberId = getMemberId(session);
        Boolean success = portfolioService.updateMemberPortfolio(memberId, portfolioUpdateDto);

        if(success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(HttpSession session, @RequestParam("portfolioId") Long portfolioId) {
        if(!sessionManager.verifySession(session)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long memberId = getMemberId(session);
        boolean isPortfolioDeleted  = portfolioService.deleteMemberPortfolio(memberId, portfolioId);

        if(isPortfolioDeleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    private Long getMemberId(HttpSession session) {
        return (Long) session.getAttribute(SessionConfig.memberId);
    }
}
