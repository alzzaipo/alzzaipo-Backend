package com.alzzaipo.notification.adapter.out.persistence.email;

import com.alzzaipo.common.BaseTimeEntity;
import com.alzzaipo.member.adapter.out.persistence.member.MemberJpaEntity;
import com.alzzaipo.notification.domain.email.EmailNotification;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "email_notification")
public class EmailNotificationJpaEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false, length = 256)
    private String email;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberJpaEntity memberJpaEntity;

    public EmailNotificationJpaEntity(String email, MemberJpaEntity memberJpaEntity) {
        this.email = email;
        this.memberJpaEntity = memberJpaEntity;
    }

    void changeEmail(String email) {
        this.email = email;
    }

    public static EmailNotificationJpaEntity build(EmailNotification domainEntity, MemberJpaEntity memberJpaEntity) {
        return new EmailNotificationJpaEntity(domainEntity.getEmail().get(), memberJpaEntity);
    }
}
