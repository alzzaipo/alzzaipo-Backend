package com.alzzaipo.common.jwt;

import com.alzzaipo.common.MemberPrincipal;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
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

    private final NewJwtUtil newJwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰 누락");
            return;
        }

        String token = authorization.split(" ")[1];

        try {
            MemberPrincipal memberPrincipal = createPrincipalFromToken(token);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    memberPrincipal,
                    null,
                    List.of(new SimpleGrantedAuthority("USER")));

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (ExpiredJwtException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰 만료");
            return;
        } catch (SignatureException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰 검증 실패");
            return;
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "토큰 오류");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private MemberPrincipal createPrincipalFromToken(String token) {
        return new MemberPrincipal(
                newJwtUtil.getMemberUID(token),
                newJwtUtil.getLoginType(token));
    }
}
