package com.alzzaipo.hexagonal.notification.adapter.in.web.criterion.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterNotificationCriterionWebRequest {

    private int competitionRate;
    private int lockupRate;
}
