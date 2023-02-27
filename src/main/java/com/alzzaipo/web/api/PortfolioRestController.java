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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            ArrayList<PortfolioListDto> error = new ArrayList<>();
            error.add(new PortfolioListDto());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(error);
        }

        Long memberId = (Long) session.getAttribute(SessionConfig.memberId);
        List<PortfolioListDto> userPortfolios = portfolioService.getPortfolioListDtosByMemberId(memberId);
        return ResponseEntity.ok(userPortfolios);
    }

    @PostMapping("/createUserPortfolio")
    public ResponseEntity<String> createUserPortfolio(HttpSession session, PortfolioSaveRequestDto portfolioSaveRequestDto) {
        if(!sessionManager.verifySession(session)) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Not a Member");
        }

        Long memberId = (Long) session.getAttribute(SessionConfig.memberId);
        portfolioSaveRequestDto.setMemberId(memberId);

        portfolioService.createPortfolio(portfolioSaveRequestDto);

        return ResponseEntity.ok("Success");
    }

}
