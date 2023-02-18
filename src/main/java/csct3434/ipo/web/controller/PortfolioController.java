package csct3434.ipo.web.controller;

import csct3434.ipo.config.SessionConfig;
import csct3434.ipo.config.SessionManager;
import csct3434.ipo.service.IPOService;
import csct3434.ipo.service.MemberService;
import csct3434.ipo.service.PortfolioService;
import csct3434.ipo.web.dto.PortfolioListDto;
import csct3434.ipo.web.dto.PortfolioSaveRequestDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class PortfolioController {

    private final SessionManager sessionManager;
    private final IPOService ipoService;
    private final MemberService memberService;
    private final PortfolioService portfolioService;

    @GetMapping("/portfolio")
    public String showPortfolioList(HttpSession session, Model model) {

        if(!sessionManager.verifySession(session)) {
            return "login";
        }

        Long memberId = (Long) session.getAttribute(SessionConfig.memberId);

        List<PortfolioListDto> portfolioListDtos = memberService.getPortfolioListDtosByMemberId(memberId);

        model.addAttribute("portfolioListDtos", portfolioListDtos);
        return "portfolio";
    }

    @GetMapping("/portfolio/new")
    public String createForm(HttpSession session, Model model) {

        if(!sessionManager.verifySession(session)) {
            return "login";
        }

        PortfolioSaveRequestDto portfolioSaveRequestDto = new PortfolioSaveRequestDto();
        Long memberId = (Long) session.getAttribute(SessionConfig.memberId);
        portfolioSaveRequestDto.setMemberId(memberId);

        model.addAttribute("portfolioSaveRequestDto", portfolioSaveRequestDto);
        model.addAttribute("ipoList", ipoService.getAllDtoList());
        return "createPortfolioForm";
    }

    @PostMapping("/portfolio/new")
    public String create(PortfolioSaveRequestDto portfolioSaveRequestDto) {
        portfolioService.createPortfolio(portfolioSaveRequestDto);

        return "redirect:/portfolio";
    }

}
