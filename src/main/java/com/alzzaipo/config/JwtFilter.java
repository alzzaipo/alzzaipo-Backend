package com.alzzaipo.config;

import com.alzzaipo.service.MemberService;
import com.alzzaipo.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final MemberService memberService;
    private final String jwtSecretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 토큰이 없으면 Block 처리
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.error("토큰이 없습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 꺼내기
        String token = authorization.split(" ")[1];

        // Token 만료 체크
        if(JwtUtil.isExpired(token, jwtSecretKey)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰에서 uid 꺼내기
        String uid = JwtUtil.getUid(token, jwtSecretKey);

        // 권한 부여
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(uid, null, List.of(new SimpleGrantedAuthority("USER")));

        // Detail 추가
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
