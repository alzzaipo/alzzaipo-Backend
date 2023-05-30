package com.alzzaipo.controller;

import com.alzzaipo.config.MemberPrincipal;
import com.alzzaipo.dto.member.MemberProfileDto;
import com.alzzaipo.dto.member.MemberProfileUpdateRequestDto;
import com.alzzaipo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/profile")
    public ResponseEntity<MemberProfileDto> getMemberProfile(@AuthenticationPrincipal MemberPrincipal memberInfo) {
        MemberProfileDto memberProfileDto = memberService.getMemberProfileDto(memberInfo);
        return ResponseEntity.ok().body(memberProfileDto);
    }

    @PutMapping("/profile/update")
    public ResponseEntity<String> updateMemberProfile(@AuthenticationPrincipal MemberPrincipal memberInfo,
                                                      @RequestBody MemberProfileUpdateRequestDto dto) {
        memberService.updateMemberProfile(memberInfo, dto);
        return ResponseEntity.ok().body("회원정보 수정 완료");
    }

    @DeleteMapping("/unregister")
    public ResponseEntity<String> unregister(@AuthenticationPrincipal MemberPrincipal memberInfo) {
        memberService.delete(memberInfo.getMemberId());
        return ResponseEntity.ok().body("회원탈퇴 완료");
    }
}
