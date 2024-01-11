package com.alzzaipo.common.captcha.application.port.in;

public interface VerifyCaptchaResponseQuery {

    boolean verify(String clientIP, String captchaResponse);
}
