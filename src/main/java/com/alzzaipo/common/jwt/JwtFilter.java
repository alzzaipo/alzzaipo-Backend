package com.alzzaipo.common.jwt;

import com.alzzaipo.common.LoginType;
import com.alzzaipo.common.MemberPrincipal;
import com.alzzaipo.common.Uid;
import com.alzzaipo.member.adapter.out.persistence.member.MemberJpaEntity;
import com.alzzaipo.member.adapter.out.persistence.member.MemberRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private final MemberRepository memberRepository;

	private static final Pattern authorizationPattern = Pattern.compile("^Bearer (?<token>[a-zA-Z0-9-._~+/]+=*)$",
		Pattern.CASE_INSENSITIVE);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws IOException, ServletException {

		String token = resolveTokenFromAuthorizationHeader(request);
		if (token == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Token Not Found");
			filterChain.doFilter(request, response);
			return;
		}

		try {
			MemberPrincipal principal = createPrincipal(token);

			AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				principal, null, List.of(new SimpleGrantedAuthority("ROLE_" + principal.getRole().name())));

			SecurityContextHolder.getContext().setAuthentication(authentication);

			filterChain.doFilter(request, response);
		} catch (ExpiredJwtException e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Token has been expired");
		} catch (SignatureException | BadCredentialsException e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Invalid Token");
		} catch (UsernameNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("User Not Found");
		} catch (Exception e) {
			logger.error(e.getMessage());
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Authentication Error");
		}
	}

	private String resolveTokenFromAuthorizationHeader(HttpServletRequest request) {
		String authorization = request.getHeader("Authorization");
		if (!StringUtils.startsWithIgnoreCase(authorization, "bearer")) {
			return null;
		}
		Matcher matcher = authorizationPattern.matcher(authorization);
		if (!matcher.matches()) {
			throw new BadCredentialsException("Bearer token is malformed");
		}
		return matcher.group("token");
	}

	private MemberPrincipal createPrincipal(String token) {
		Uid memberId = JwtUtil.getMemberUID(token);
		LoginType loginType = JwtUtil.getLoginType(token);

		MemberJpaEntity member = memberRepository.findById(memberId.get())
			.orElseThrow(() -> new UsernameNotFoundException("회원 조회 실패"));

		return new MemberPrincipal(memberId, loginType, member.getRole());
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		List<String> excludePath = Arrays.asList(
			"/member/verify-account-id",
			"/member/verify-email",
			"/member/register",
			"/member/login",
			"/ipo",
			"/email",
			"/oauth/kakao/login");

		String path = request.getRequestURI();

		return excludePath.stream().anyMatch(path::startsWith);
	}
}
