package com.alzzaipo.controller;

import com.alzzaipo.service.IpoScraperService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class IpoScraperController {

    private final IpoScraperService ipoScraperService;

    @Value("${crawler.verificationCode}")
    private String crawlerVerificationCode;

    @GetMapping("/crawler/{code}/{startPage}/{endPage}")
    @ResponseBody
    public ResponseEntity<String> initialize(@PathVariable String code, @PathVariable int startPage, @PathVariable int endPage) {
        int updateCnt = 0;

        if(code.equals(crawlerVerificationCode)) {
            updateCnt = ipoScraperService.updateIpoTableByScrapingPages(startPage, endPage);
        }

        return ResponseEntity.ok("[성공] " + updateCnt + "건 업데이트 완료");
    }
}
