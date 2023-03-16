package com.alzzaipo.domain.notificationEmail;

import com.alzzaipo.domain.member.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class NotificationEmail {
    @Id @GeneratedValue
    @Column(name = "notification_email_id")
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public NotificationEmail(String email, Member member) {
        this.email = email;
        this.member = member;
    }
}
