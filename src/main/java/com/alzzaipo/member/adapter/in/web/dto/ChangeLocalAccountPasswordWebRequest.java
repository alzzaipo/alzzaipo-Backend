package com.alzzaipo.member.adapter.in.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChangeLocalAccountPasswordWebRequest {

    private String currentAccountPassword;
    private String newAccountPassword;
}
