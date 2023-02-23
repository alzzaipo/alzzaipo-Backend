package com.alzzaipo.config;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SessionManager {

    public Boolean verifySession(HttpSession session) {
        String accessToken = (String) session.getAttribute(SessionConfig.accessToken);

        if(accessToken == null || accessToken.equals("")) {
            return false;
        }

        return true;
    }
}
