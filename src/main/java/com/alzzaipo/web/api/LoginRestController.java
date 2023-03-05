package com.alzzaipo.web.api;

import com.alzzaipo.config.SessionConfig;
import com.alzzaipo.config.SessionManager;
import com.alzzaipo.web.dto.LoginStatusDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class LoginRestController {

    private final SessionManager sessionManager;

    @GetMapping("/api/login/status")
    public ResponseEntity<LoginStatusDto> getLoginStatus(HttpSession session) {
        if(sessionManager.verifySession(session)) {
            return ResponseEntity.ok(new LoginStatusDto(true, (String)session.getAttribute(SessionConfig.nickname)));
        } else {
            return ResponseEntity.ok(new LoginStatusDto(false, "none"));
        }
    }
}
