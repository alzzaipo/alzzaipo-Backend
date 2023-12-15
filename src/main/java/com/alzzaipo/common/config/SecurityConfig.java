package com.alzzaipo.common.config;

import com.alzzaipo.member.domain.member.Role;
import com.alzzaipo.member.adapter.out.persistence.member.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

	private final MemberRepository memberRepository;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.cors().and()
			.formLogin().disable()
			.httpBasic().disable();

		http.addFilterBefore(new JwtFilter(memberRepository), UsernamePasswordAuthenticationFilter.class);

		http.authorizeHttpRequests(requests -> requests
			.requestMatchers("/member/verify-account-id",
				"/member/verify-email",
				"/member/register",
				"/member/login",
				"/ipo/**",
				"/email/**",
				"/login/**").permitAll()
			.requestMatchers("/scraper").hasRole(Role.ADMIN.name())
			.anyRequest().authenticated());

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(List.of("https://alzzaipo.com"));
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		config.addAllowedHeader("*");
		config.setAllowCredentials(true);
		config.addExposedHeader("Authorization");

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
}
