package com.alzzaipo.common.captcha.application.service;

import com.alzzaipo.common.captcha.application.port.in.VerifyCaptchaResponseQuery;
import com.alzzaipo.common.captcha.application.port.out.VerifyCaptchaResponsePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CaptchaService implements VerifyCaptchaResponseQuery {

    private final VerifyCaptchaResponsePort verifyCaptchaResponsePort;

    @Override
    public boolean verify(String clientIP, String captchaResponse) {
        return verifyCaptchaResponsePort.verify(clientIP, captchaResponse);
    }
}
