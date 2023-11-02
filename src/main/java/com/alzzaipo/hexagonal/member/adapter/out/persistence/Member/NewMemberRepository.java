package com.alzzaipo.hexagonal.member.adapter.out.persistence.Member;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NewMemberRepository extends JpaRepository<MemberJpaEntity, Long> {
}
