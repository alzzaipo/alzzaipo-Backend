package csct3434.ipo.web.controller;

import csct3434.ipo.config.SessionConfig;
import csct3434.ipo.service.MemberService;
import csct3434.ipo.web.domain.Member.Member;
import csct3434.ipo.web.domain.Portfolio.Portfolio;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class PortfolioController {

    private final MemberService memberService;

    @GetMapping("/portfolio")
    public String portfolio(HttpSession session, Model model) {
        String accessToken = (String) session.getAttribute(SessionConfig.accessToken);
        String email = (String) session.getAttribute(SessionConfig.email);

        if(accessToken == null || accessToken.equals("")) {
            return "login";
        }

        Member member = memberService.findByEmail(email);

        if(member == null) {
            return "login";
        }

        List<Portfolio> portfolios = memberService.getPortfoliosByMember(member);
        model.addAttribute("portfolios", portfolios);
        return "portfolio";
    }
}
