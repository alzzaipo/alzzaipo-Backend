package com.alzzaipo.ipo.adapter.in.web;

import com.alzzaipo.ipo.adapter.in.web.dto.ScrapeAndRegisterIposWebRequest;
import com.alzzaipo.ipo.application.port.in.ScrapeAndRegisterIposUseCase;
import com.alzzaipo.ipo.application.port.out.dto.ScrapeIposCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class IpoScrapingController {

    private final ScrapeAndRegisterIposUseCase scrapeAndRegisterIposUseCase;

    @Value("${crawler.verificationCode}")
    private String authorizationCode;

    @PostMapping("/scraper")
    public ResponseEntity<String> scrapeAndRegisterIpos(
            @RequestBody ScrapeAndRegisterIposWebRequest scrapeAndRegisterIposWebRequest) {

        if (!checkAuthorizationCode(scrapeAndRegisterIposWebRequest.getAuthorizationCode())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");
        }

        ScrapeIposCommand scrapeIposCommand = new ScrapeIposCommand(
                scrapeAndRegisterIposWebRequest.getPageFrom(),
                scrapeAndRegisterIposWebRequest.getPageTo());

        int totalRegistrationCount =
                scrapeAndRegisterIposUseCase.scrapeAndRegisterIposUseCase(scrapeIposCommand);

        return ResponseEntity.ok("[성공] " + totalRegistrationCount + "건 업데이트 완료");
    }

    private boolean checkAuthorizationCode(String userInputAuthorizationCode) {
        return userInputAuthorizationCode.equals(authorizationCode);
    }
}
