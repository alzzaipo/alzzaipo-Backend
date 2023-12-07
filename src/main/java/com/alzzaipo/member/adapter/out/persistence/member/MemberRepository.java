package com.alzzaipo.member.adapter.out.persistence.member;

import com.alzzaipo.member.adapter.out.persistence.member.custom.MemberRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberJpaEntity, Long>,
    MemberRepositoryCustom {

    @Query("SELECT m FROM MemberJpaEntity m WHERE m.uid = :uid")
    Optional<MemberJpaEntity> findByUid(@Param("uid") Long uid);
}
