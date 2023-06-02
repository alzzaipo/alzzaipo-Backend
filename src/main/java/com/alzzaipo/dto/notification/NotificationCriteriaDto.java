package com.alzzaipo.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationCriteriaDto {
    private Long id;
    private int competitionRate;
    private int lockupRate;
}