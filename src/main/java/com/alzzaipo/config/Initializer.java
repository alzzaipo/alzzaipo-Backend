package com.alzzaipo.config;

import com.alzzaipo.crawler.Crawler;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class Initializer {

    private final Crawler crawler;
    private final Environment env;

    @GetMapping("/crawler/{code}/{year}")
    @ResponseBody
    public String initialize(@PathVariable String code, @PathVariable int year) {
        String crawlerCode = env.getProperty("crawlerCode");

        if(code.equals(crawlerCode)) {
            crawler.updateIPOListFrom(year);
        }

        return "index";
    }
}
