package com.alzzaipo.api;

import com.alzzaipo.domain.dto.MemberJoinRequestDto;
import com.alzzaipo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RequestMapping("/api/member")
@RestController
public class MemberRestController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody MemberJoinRequestDto dto) {
        memberService.join(dto.getUid(), dto.getPassword(), dto.getEmail(), dto.getNickname());
        return ResponseEntity.ok().body("회원가입이 완료되었습니다.");
    }
}
