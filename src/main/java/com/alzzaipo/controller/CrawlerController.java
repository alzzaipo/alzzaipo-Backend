package com.alzzaipo.controller;

import com.alzzaipo.service.CrawlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class CrawlerController {

    private final CrawlerService crawlerService;

    @Value("${crawler.verificationCode}")
    private String crawlerVerificationCode;

    @GetMapping("/crawler/{code}/{year}")
    @ResponseBody
    public ResponseEntity<String> initialize(@PathVariable String code, @PathVariable int year) {
        int updateCnt = 0;

        if(code.equals(crawlerVerificationCode)) {
            updateCnt = crawlerService.updateIPOListFrom(year);
        }

        return ResponseEntity.ok("[성공] " + updateCnt + "건 업데이트 완료");
    }
}
