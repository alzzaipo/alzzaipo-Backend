package com.alzzaipo.common.captcha.application.port.out;

public interface VerifyCaptchaResponsePort {

    boolean verify(String clientIP, String captchaResponse);
}
