package com.alzzaipo.common.captcha.adapter.out.captcha;

import com.alzzaipo.common.captcha.adapter.out.captcha.dto.ReCaptchaVerificationResponse;
import com.alzzaipo.common.captcha.application.port.out.VerifyCaptchaResponsePort;
import java.net.URI;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class ReCaptchaAdapter implements VerifyCaptchaResponsePort {

    @Value("${google.recaptcha.secret}")
    private String reCaptchaSecret;

    private static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");

    @Override
    public boolean verify(String clientIP, String captchaResponse) {
        if (!checkSanity(captchaResponse)) {
            return false;
        }

        RestTemplate restTemplate = new RestTemplate();

        URI verifyUri = URI.create(String.format(
            "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s",
            reCaptchaSecret, captchaResponse, clientIP));

        ReCaptchaVerificationResponse verificationResponse = restTemplate.exchange(verifyUri, HttpMethod.POST, null,
            ReCaptchaVerificationResponse.class).getBody();

        return verificationResponse != null && verificationResponse.isSuccess();
    }

    private boolean checkSanity(String captchaResponse) {
        return StringUtils.hasLength(captchaResponse) && RESPONSE_PATTERN.matcher(captchaResponse).matches();
    }
}
