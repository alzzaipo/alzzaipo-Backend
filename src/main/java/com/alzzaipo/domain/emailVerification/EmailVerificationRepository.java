package com.alzzaipo.domain.emailVerification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
    @Query("SELECT e.verificationCode FROM EmailVerification e where e.email = ?1")
    Optional<String> findVerificationCodeByEmail(String email);

    @Query
    Optional<EmailVerification> findByEmail(String email);

    @Modifying
    @Query("UPDATE EmailVerification ev SET ev.verificationStatus = true WHERE ev.email = ?1")
    void changeVerificationStatusToVerified(String email);
}
