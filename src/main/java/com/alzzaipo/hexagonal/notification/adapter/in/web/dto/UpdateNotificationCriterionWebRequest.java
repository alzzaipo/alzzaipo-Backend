package com.alzzaipo.hexagonal.notification.adapter.in.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateNotificationCriterionWebRequest {

    private Long uid;
    private int competitionRate;
    private int lockupRate;
}
