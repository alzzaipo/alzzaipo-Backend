package com.alzzaipo.email.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmailVerificationHistoryRepository extends JpaRepository<EmailVerificationHistoryJpaEntity, Long> {
    @Query("SELECT e FROM EmailVerificationHistoryJpaEntity e WHERE e.email = :email")
    Optional<EmailVerificationHistoryJpaEntity> findByEmail(@Param("email") String email);
}
