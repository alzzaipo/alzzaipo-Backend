package com.alzzaipo.member.adapter.out.persistence.member;

import com.alzzaipo.member.adapter.out.persistence.member.custom.MemberRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberJpaEntity, Long>, MemberRepositoryCustom {

}
