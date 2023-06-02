package com.alzzaipo.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class NotificationCriteriaListDto {
    List<NotificationCriteriaDto> notificationCriteriaList;
}
