package com.alzzaipo.config;

import com.alzzaipo.crawler.Crawler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class Initializer {

    private final Crawler crawler;

    @GetMapping("/crawler/{code}/{year}")
    @ResponseBody
    public String initialize(@PathVariable String code, @PathVariable int year) {
        String answer = System.getenv("CRAWLER_CODE");

        if(code.equals(answer)) {
            crawler.updateIPOListFrom(year);
        }

        return "index";
    }
}
