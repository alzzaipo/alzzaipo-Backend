package com.alzzaipo.member.adapter.out.persistence.member;

import com.alzzaipo.common.BaseTimeEntity;
import com.alzzaipo.member.adapter.out.persistence.account.local.LocalAccountJpaEntity;
import com.alzzaipo.member.adapter.out.persistence.account.social.SocialAccountJpaEntity;
import com.alzzaipo.member.domain.member.Member;
import com.alzzaipo.member.domain.member.Role;
import com.alzzaipo.notification.adapter.out.persistence.criterion.NotificationCriterionJpaEntity;
import com.alzzaipo.portfolio.adapter.out.persistence.PortfolioJpaEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "member")
public class MemberJpaEntity extends BaseTimeEntity {

	@Id
	private Long id;

	@Column(nullable = false)
	private String nickname;

	@OneToOne(mappedBy = "memberJpaEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private LocalAccountJpaEntity localAccountJpaEntity;

	@OneToMany(mappedBy = "memberJpaEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private final List<SocialAccountJpaEntity> socialAccountJpaEntities = new ArrayList<>();

	@OneToMany(mappedBy = "memberJpaEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private final List<PortfolioJpaEntity> portfolioJpaEntities = new ArrayList<>();

	@OneToMany(mappedBy = "memberJpaEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private final List<NotificationCriterionJpaEntity> notificationCriteriaJpaEntities = new ArrayList<>();

	@Enumerated(EnumType.STRING)
	private Role role;

	public MemberJpaEntity(Long id, String nickname) {
		this.id = id;
		this.nickname = nickname;
		this.role = Role.USER;
	}

	public void changeNickname(String nickname) {
		this.nickname = nickname;
	}

	public Member toDomainEntity() {
		return new Member(
			new com.alzzaipo.common.Id(id),
			nickname,
			role);
	}
}
