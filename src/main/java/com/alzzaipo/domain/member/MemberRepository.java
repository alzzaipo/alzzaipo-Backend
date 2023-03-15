package com.alzzaipo.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query
    Optional<Member> findByEmail(String email);

    Optional<Member> findByUid(String uid);
}
