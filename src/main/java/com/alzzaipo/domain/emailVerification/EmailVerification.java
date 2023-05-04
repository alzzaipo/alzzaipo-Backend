package com.alzzaipo.domain.emailVerification;

import com.alzzaipo.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class EmailVerification extends BaseTimeEntity {
    @Id @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false, length = 256)
    private String email;

    @Column(name = "verification_code", nullable = false, length = 8)
    private String verificationCode;

    @Column(name = "verification_status", columnDefinition = "boolean default false")
    private boolean verificationStatus;

    @Builder
    public EmailVerification(String email, String verificationCode) {
        this.email = email;
        this.verificationCode = verificationCode;
    }

    public void setVerifiedStatus() {
        this.verificationStatus = true;
    }
}