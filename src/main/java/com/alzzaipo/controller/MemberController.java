package com.alzzaipo.controller;

import com.alzzaipo.config.MemberPrincipal;
import com.alzzaipo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class MemberController {

    private final MemberService memberService;

    @DeleteMapping("/unregister")
    public ResponseEntity<String> unregister(@AuthenticationPrincipal MemberPrincipal memberInfo) {
        memberService.delete(memberInfo.getMemberId());
        return ResponseEntity.ok().body("회원탈퇴 완료");
    }
}
