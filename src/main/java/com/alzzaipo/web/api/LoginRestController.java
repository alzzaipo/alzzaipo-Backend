package com.alzzaipo.web.api;

import com.alzzaipo.config.SessionManager;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class LoginRestController {

    private final SessionManager sessionManager;

    @GetMapping("/api/getLoginStatus")
    public ResponseEntity<Boolean> getLoginStatus(HttpSession session) {

        Boolean login = sessionManager.verifySession(session);

        return ResponseEntity.ok(login);
    }
}
