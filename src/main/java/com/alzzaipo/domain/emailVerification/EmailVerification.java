package com.alzzaipo.domain.emailVerification;

import com.alzzaipo.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
public class EmailVerification extends BaseTimeEntity {
    @Id @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false, length = 256)
    private String email;

    @Column(name = "authcode", nullable = false, length = 8)
    private String authCode;

    @Builder
    public EmailVerification(String email, String authCode) {
        this.email = email;
        this.authCode = authCode;
    }
}