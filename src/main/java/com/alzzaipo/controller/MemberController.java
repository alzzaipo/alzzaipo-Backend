package com.alzzaipo.controller;

import com.alzzaipo.config.MemberPrincipal;
import com.alzzaipo.enums.LoginType;
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
    public ResponseEntity<String> getMemberNickname(@AuthenticationPrincipal MemberPrincipal memberInfo) {
        String nickname = memberService.getMemberNickname(memberInfo.getMemberId());
        return ResponseEntity.ok().body(nickname);
    }

    @GetMapping("/member-type")
    public ResponseEntity<MemberType> getMemberType(@AuthenticationPrincipal MemberPrincipal memberInfo) {
        MemberType memberType = memberService.getMemberType(memberInfo.getMemberId());
        return ResponseEntity.ok().body(memberType);
    }

    @GetMapping("/current-login-type")
    public ResponseEntity<LoginType> getCurrentLoginType(@AuthenticationPrincipal MemberPrincipal memberInfo) {
        return ResponseEntity.ok().body(memberInfo.getLoginType());
    }
}
