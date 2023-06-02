package com.alzzaipo.domain.notification.email;

import com.alzzaipo.domain.member.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class EmailNotification {
    @Id @GeneratedValue
    @Column(name = "email_notification_id")
    private Long id;

    @Column(name = "email", nullable = false, unique = true, length = 256)
    private String email;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public EmailNotification(String email, Member member) {
        this.email = email;
        this.member = member;
    }

    public void changeEmail(String email) {
        this.email = email;
    }
}
