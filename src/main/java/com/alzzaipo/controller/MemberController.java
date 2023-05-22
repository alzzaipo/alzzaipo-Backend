package com.alzzaipo.controller;

import com.alzzaipo.enums.MemberType;
import com.alzzaipo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/member")
@RestController
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/nickname")
    public ResponseEntity<String> getMemberNickname(@AuthenticationPrincipal Long memberId) {
        String nickname = memberService.getMemberNickname(memberId);
        return ResponseEntity.ok().body(nickname);
    }

    @GetMapping("/memberType")
    public ResponseEntity<MemberType> getMemberType(@AuthenticationPrincipal Long memberId) {
        MemberType memberType = memberService.getMemberType(memberId);
        return ResponseEntity.ok().body(memberType);
    }
}
