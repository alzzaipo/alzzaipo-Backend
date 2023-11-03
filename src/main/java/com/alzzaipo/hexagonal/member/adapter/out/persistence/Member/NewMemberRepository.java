package com.alzzaipo.hexagonal.member.adapter.out.persistence.Member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NewMemberRepository extends JpaRepository<MemberJpaEntity, Long> {

    @Query("SELECT m FROM MemberJpaEntity m WHERE m.uid = :uid")
    Optional<MemberJpaEntity> findByUid(@Param("uid") Long uid);
}
