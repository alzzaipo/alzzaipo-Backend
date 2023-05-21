package com.alzzaipo.dto.account.local;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LocalAccountProfileUpdateRequestDto {
    private String nickname;
    private String email;
}
