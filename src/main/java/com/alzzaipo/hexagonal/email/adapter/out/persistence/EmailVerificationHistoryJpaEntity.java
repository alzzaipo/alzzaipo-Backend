package com.alzzaipo.hexagonal.email.adapter.out.persistence;

import com.alzzaipo.hexagonal.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class EmailVerificationHistoryJpaEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false, length = 256)
    private String email;

    @Column(name = "verification_code", nullable = false, length = 8)
    private String verificationCode;

    @Column(name = "verified")
    private boolean verified;

    public EmailVerificationHistoryJpaEntity(String email, String verificationCode) {
        this.email = email;
        this.verificationCode = verificationCode;
    }

    public void setVerified() {
        this.verified = true;
    }
}
