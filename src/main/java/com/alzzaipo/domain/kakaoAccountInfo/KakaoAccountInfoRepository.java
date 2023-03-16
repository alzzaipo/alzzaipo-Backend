package com.alzzaipo.domain.kakaoAccountInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface KakaoAccountInfoRepository extends JpaRepository<KakaoAccountInfo, Long> {
    @Query("SELECT k.kakaoAccountId FROM KakaoAccountInfo k WHERE k.member.accountId = :accountId")
    Optional<Long> findKakaoAccountIdByMemberAccountId(String accountId);
}
