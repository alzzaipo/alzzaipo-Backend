package com.alzzaipo.member.adapter.out.persistence.member;

import com.alzzaipo.member.adapter.out.persistence.member.custom.MemberRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberJpaEntity, Long>, MemberRepositoryCustom {

}
