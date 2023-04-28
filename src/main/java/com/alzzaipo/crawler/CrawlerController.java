package com.alzzaipo.crawler;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class CrawlerController {

    private final Crawler crawler;

    @Value("${crawler.authCode}")
    private String crawlerAuthCode;

    @GetMapping("/crawler/{code}/{year}")
    @ResponseBody
    public String initialize(@PathVariable String code, @PathVariable int year) {

        if(code.equals(crawlerAuthCode)) {
            crawler.updateIPOListFrom(year);
        }

        return "redirect:/";
    }
}
