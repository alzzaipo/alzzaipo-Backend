package com.alzzaipo.dto.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberProfileUpdateRequestDto {
    private String nickname;
    private String email;
}
