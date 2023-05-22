package com.alzzaipo.dto.account.local;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class LocalAccountPasswordChangeRequestDto {
    String currentAccountPassword;
    String newAccountPassword;
}
