package com.alzzaipo.domain.emailVerification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
    @Query("SELECT e.authCode FROM EmailVerification e where e.email = :email")
    Optional<String> findAuthCodeByEmail(String email);
}
