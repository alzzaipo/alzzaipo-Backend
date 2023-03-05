package com.alzzaipo.config;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SessionManager {

    public Boolean verifySession(HttpSession session) {
        Long memberId = (Long) session.getAttribute(SessionConfig.memberId);

        return (memberId != null && !memberId.equals(0));
    }
}
