package com.alzzaipo.web.api;

import com.alzzaipo.config.SessionConfig;
import com.alzzaipo.config.SessionManager;
import com.alzzaipo.service.PortfolioService;
import com.alzzaipo.web.dto.PortfolioListDto;
import com.alzzaipo.web.dto.PortfolioSaveRequestDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class PortfolioRestController {

    private final PortfolioService portfolioService;
    private final SessionManager sessionManager;

    @GetMapping("/getUserPortfolios")
    public ResponseEntity<List<PortfolioListDto>> getUserPortfolios(HttpSession session) {
        if(!sessionManager.verifySession(session)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ArrayList<>());
        }

        Long memberId = (Long) session.getAttribute(SessionConfig.memberId);
        List<PortfolioListDto> memberPortfolios = portfolioService.getMemberPortfolioListDtos(memberId);

        if(memberPortfolios.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ArrayList<>());
        }

        return ResponseEntity.ok(memberPortfolios);
    }

    @PostMapping("/createUserPortfolio")
    public ResponseEntity createUserPortfolio(HttpSession session, PortfolioSaveRequestDto portfolioSaveRequestDto) {
        if(!sessionManager.verifySession(session)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long memberId = (Long) session.getAttribute(SessionConfig.memberId);
        Boolean success = portfolioService.createMemberPortfolio(memberId, portfolioSaveRequestDto);

        if(success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("updateUserPortfolio")
    public ResponseEntity updateUserPortfolio(HttpSession session, PortfolioSaveRequestDto portfolioSaveRequestDto) {
        if(!sessionManager.verifySession(session)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long memberId = (Long) session.getAttribute(SessionConfig.memberId);
        Boolean success = portfolioService.updateMemberPortfolio(memberId, portfolioSaveRequestDto);

        if(success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
