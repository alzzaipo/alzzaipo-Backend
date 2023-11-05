package com.alzzaipo.hexagonal.member.adapter.in.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateMemberProfileWebRequest {

    private String nickname;
    private String email;
}
