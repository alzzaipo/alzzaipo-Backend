package com.alzzaipo.api;

import com.alzzaipo.domain.dto.MemberJoinRequestDto;
import com.alzzaipo.domain.dto.MemberLoginRequestDto;
import com.alzzaipo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RequestMapping("/api/member")
@RestController
public class MemberRestController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<String> join(MemberJoinRequestDto dto) {
        memberService.join(dto.getAccountId(), dto.getAccountPassword(), dto.getEmail(), dto.getNickname());
        return ResponseEntity.ok().body("회원가입이 성공했습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(MemberLoginRequestDto dto) {
        String token = memberService.login(dto.getAccountId(), dto.getAccountPassword());
        return ResponseEntity.ok().body(token);
    }
}
