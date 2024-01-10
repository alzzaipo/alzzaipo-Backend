package com.alzzaipo.notification.adapter.in.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SubscribeEmailNotificationRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String verificationCode;
}
